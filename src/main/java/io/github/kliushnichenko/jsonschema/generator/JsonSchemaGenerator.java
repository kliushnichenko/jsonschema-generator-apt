package io.github.kliushnichenko.jsonschema.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kliushnichenko.jsonschema.model.*;
import lombok.NoArgsConstructor;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for generating JSON schema from method parameters.
 */
@NoArgsConstructor
public class JsonSchemaGenerator {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private AnnotationsMappingProcessor annotationsMappingProcessor = new AnnotationsMappingProcessor(Map.of());

    public JsonSchemaGenerator(Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> mappers) {
        this.annotationsMappingProcessor = new AnnotationsMappingProcessor(mappers);
    }

    /**
     * Generates JSON schema as a string for the given method parameters.
     */
    @SuppressWarnings("unchecked")
    public String generate(ExecutableElement method) {
        JsonSchemaObj schema = new JsonSchemaObj();
        populateSchemaFromParams(schema, (List<VariableElement>) method.getParameters());
        return serializeSchemaObj(schema);
    }

    /**
     * Generates JSON schema object for the given method parameters.
     */
    @SuppressWarnings("unchecked")
    public JsonSchemaObj generateAsObject(ExecutableElement method) {
        JsonSchemaObj schema = new JsonSchemaObj();
        populateSchemaFromParams(schema, (List<VariableElement>) method.getParameters());
        return schema;
    }

    private void populateSchemaFromParams(JsonSchemaObj schema, List<VariableElement> parameters) {
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> required = new ArrayList<>();

        for (VariableElement parameter : parameters) {
            processParameter(parameter, properties, required);
        }

        schema.setProperties(properties);
        schema.setRequired(required);
    }

    private void processParameter(VariableElement parameter, Map<String, Object> properties, List<String> required) {
        JsonSchemaProps schemaProps = annotationsMappingProcessor.evalJsonSchemaProps(parameter);
        TypeMirror paramTypeMirror = parameter.asType();
        String propName = schemaProps.getName();

        if (TypeUtils.isIterableType(paramTypeMirror)) {
            properties.put(propName, buildSchemaForIterable(paramTypeMirror, schemaProps));
        } else if (TypeUtils.isMapType(paramTypeMirror)) {
            properties.put(propName, buildSchemaForMap(paramTypeMirror, schemaProps));
        } else if (TypeUtils.isCustomObjectType(paramTypeMirror)) {
            properties.put(propName, buildSchemaForCustomObject(parameter, schemaProps));
        } else {
            properties.put(propName, buildSchemaForScalarType(paramTypeMirror, schemaProps));
        }

        if (schemaProps.isRequired()) {
            required.add(propName);
        }
    }

    private JsonSchemaScalar buildSchemaForScalarType(TypeMirror paramTypeMirror, JsonSchemaProps schemaProps) {
        JsonSchemaTypeInfo typeInfo = TypeMapper.toJsonSchemaTypeInfo(paramTypeMirror);
        JsonSchemaScalar scalarTypeSchema = new JsonSchemaScalar(typeInfo.getType());

        if (JsonSchemaType.STRING == typeInfo.getType()) {
            scalarTypeSchema.setFormat(typeInfo.getFormat());
        }

        enrichSchema(schemaProps, scalarTypeSchema);
        return scalarTypeSchema;
    }

    private JsonSchemaObj buildSchemaForCustomObject(VariableElement parameter, JsonSchemaProps schemaProps) {
        JsonSchemaObj objectSchema = new JsonSchemaObj();
        List<VariableElement> fields = resolveObjectFields(parameter.asType());
        populateSchemaFromParams(objectSchema, fields);
        enrichSchema(schemaProps, objectSchema);
        return objectSchema;
    }

    private JsonSchemaObj buildSchemaForMap(TypeMirror paramTypeMirror, JsonSchemaProps schemaProps) {
        JsonSchemaObj mapSchema = new JsonSchemaObj();
        TypeMirror type = TypeUtils.getMapValueType(paramTypeMirror);

        Object additionalProps;
        if ("java.lang.Object".equals(TypeUtils.getTypeName(type))) {
            additionalProps = Map.of("oneOf", List.of(
                    new JsonSchemaBase(JsonSchemaType.STRING),
                    new JsonSchemaBase(JsonSchemaType.NUMBER),
                    new JsonSchemaBase(JsonSchemaType.INTEGER),
                    new JsonSchemaBase(JsonSchemaType.BOOLEAN),
                    new JsonSchemaBase(JsonSchemaType.OBJECT),
                    new JsonSchemaBase(JsonSchemaType.ARRAY),
                    new JsonSchemaBase(JsonSchemaType.NULL)
            ));
        } else {
            additionalProps = buildSchemaForType(type);
        }
        mapSchema.setAdditionalProperties(additionalProps);
        enrichSchema(schemaProps, mapSchema);
        return mapSchema;
    }

    private JsonSchemaArray buildSchemaForIterable(TypeMirror paramTypeMirror, JsonSchemaProps schemaProps) {
        JsonSchemaArray arraySchema = new JsonSchemaArray();
        TypeMirror type = TypeUtils.getCollectionComponentType(paramTypeMirror);
        JsonSchemaBase itemsSchema = buildSchemaForType(type);
        arraySchema.setItems(itemsSchema);
        enrichSchema(schemaProps, arraySchema);
        return arraySchema;
    }

    private void enrichSchema(JsonSchemaProps schemaProps, JsonSchemaBase schema) {
        if (isPresent(schemaProps.getDescription())) {
            schema.setDescription(schemaProps.getDescription());
        }

        if (isPresent(schemaProps.getDefaultValue())) {
            schema.setDefaultValue(schemaProps.getDefaultValue());
        }
    }

    private boolean isPresent(String str) {
        return str != null && !str.isEmpty();
    }

    private JsonSchemaBase buildSchemaForType(TypeMirror typeMirror) {
        if (typeMirror instanceof DeclaredType) {
            if (TypeUtils.isBuiltInType(typeMirror)) {
                JsonSchemaTypeInfo typeInfo = TypeMapper.toJsonSchemaTypeInfo(typeMirror);
                return new JsonSchemaBase(typeInfo);
            } else {
                // Custom object type
                JsonSchemaObj objectSchema = new JsonSchemaObj();
                List<VariableElement> fields = resolveObjectFields(typeMirror);
                populateSchemaFromParams(objectSchema, fields);
                return objectSchema;
            }
        }

        // Handle primitive types
        JsonSchemaTypeInfo typeInfo = TypeMapper.toJsonSchemaTypeInfo(typeMirror);
        return new JsonSchemaBase(typeInfo);
    }

    @SuppressWarnings("unchecked")
    private List<VariableElement> resolveObjectFields(TypeMirror typeMirror) {
        TypeElement typeElement = (TypeElement) ((DeclaredType) typeMirror).asElement();
        return (List<VariableElement>) typeElement.getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .toList();
    }

    public static String serializeSchemaObj(JsonSchemaObj schema) {
        try {
            return OBJECT_MAPPER.writeValueAsString(schema);
        } catch (Exception e) {
            throw new JsonSchemaSerializationException("Failed to serialize JSON schema", e);
        }
    }
}
