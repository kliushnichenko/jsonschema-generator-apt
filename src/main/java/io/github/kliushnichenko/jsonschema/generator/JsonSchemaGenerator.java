package io.github.kliushnichenko.jsonschema.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kliushnichenko.jsonschema.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.*;

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
     *
     * @param method Method element to generate schema for
     */
    public String generate(ExecutableElement method) {
        return generate(method, Set.of());
    }

    /**
     * Generates JSON schema as a string for the given type.
     *
     * @param typeMirror Type mirror to generate schema for
     * @return JSON schema as a string, or null if the type is not a custom object type
     */
    public String generate(TypeMirror typeMirror) {
        JsonSchemaBase schema;
        var props = new JsonSchemaProps();
        if (TypeUtils.isIterableType(typeMirror)) {
            schema = buildSchemaForIterable(typeMirror, props);
        } else if (TypeUtils.isMapType(typeMirror)) {
            schema = buildSchemaForMap(typeMirror, props);
        } else if (TypeUtils.isCustomObjectType(typeMirror)) {
            schema = buildSchemaForCustomObject(typeMirror, props);
        } else {
            return null;
        }
        return serializeSchema(schema);
    }

    /**
     * Generates JSON schema as a string for the given method parameters.
     *
     * @param method      Method element to generate schema for
     * @param ignoreTypes Set of fully qualified type names to ignore during schema generation
     */
    @SuppressWarnings("unchecked")
    public String generate(ExecutableElement method, Set<String> ignoreTypes) {
        JsonSchemaObj schema = new JsonSchemaObj();
        var parameters = filterOutIgnoredTypes((List<VariableElement>) method.getParameters(), ignoreTypes);
        populateSchemaFromParams(schema, parameters);
        return serializeSchemaObj(schema);
    }

    public JsonSchemaObj generateAsObject(ExecutableElement method) {
        return generateAsObject(method, Set.of());
    }

    /**
     * Generates JSON schema object for the given method parameters.
     *
     * @param method      Method element to generate schema for
     * @param ignoreTypes Set of fully qualified type names to ignore during schema generation
     */
    @SuppressWarnings("unchecked")
    public JsonSchemaObj generateAsObject(ExecutableElement method, Set<String> ignoreTypes) {
        JsonSchemaObj schema = new JsonSchemaObj();
        var parameters = filterOutIgnoredTypes((List<VariableElement>) method.getParameters(), ignoreTypes);
        populateSchemaFromParams(schema, parameters);
        return schema;
    }

    private List<VariableElement> filterOutIgnoredTypes(List<VariableElement> parameters, Set<String> ignoreTypes) {
        return parameters.stream()
                .filter(param -> !ignoreTypes.contains(TypeUtils.getTypeName(param.asType())))
                .toList();
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
            if (TypeUtils.isEnum(paramTypeMirror)) {
                properties.put(propName, buildSchemaForEnum(parameter.asType(), schemaProps));
            } else {
                properties.put(propName, buildSchemaForCustomObject(parameter.asType(), schemaProps));
            }
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

    private JsonSchemaObj buildSchemaForCustomObject(TypeMirror typeMirror, JsonSchemaProps schemaProps) {
        JsonSchemaObj objectSchema = new JsonSchemaObj();

        TypeElement typeElement = toElement(typeMirror);
        Schema classLevelSchema = typeElement.getAnnotation(Schema.class);
        if (classLevelSchema != null) {
            if (isPresent(classLevelSchema.description())) {
                objectSchema.setDescription(classLevelSchema.description());
            }
        }

        List<VariableElement> fields = resolveObjectFields(typeMirror);
        populateSchemaFromParams(objectSchema, fields);
        enrichSchema(schemaProps, objectSchema);

        if (schemaProps.isAdditionalProperties()) {
            // override if explicitly set via annotation
            objectSchema.setAdditionalProperties(true);
        }

        if (schemaProps.getTypes() != null) {
            // override type if explicitly set via annotation
            objectSchema.setType(schemaProps.getTypes());
        }

        return objectSchema;
    }

    private JsonSchemaScalar buildSchemaForEnum(TypeMirror typeMirror, JsonSchemaProps schemaProps) {
        JsonSchemaScalar schema = new JsonSchemaScalar(JsonSchemaType.STRING);

        TypeElement typeElement = toElement(typeMirror);
        Schema classLevelSchema = typeElement.getAnnotation(Schema.class);
        if (classLevelSchema != null) {
            if (!isPresent(schemaProps.getDescription()) && isPresent(classLevelSchema.description())) {
                schema.setDescription(classLevelSchema.description());
            }

            if (classLevelSchema.allowableValues().length > 0 && schemaProps.getAllowableValues() == null) {
                // override enum values if explicitly at class-level
                schemaProps.setAllowableValues(List.of(classLevelSchema.allowableValues()));
            }
        }

        enrichSchema(schemaProps, schema);

        List<String> enumValues = typeElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.ENUM_CONSTANT)
                .map(e -> e.getSimpleName().toString())
                .toList();

        if (schemaProps.getAllowableValues() != null) {
            // override enum values if explicitly set via annotation
            enumValues = schemaProps.getAllowableValues();
        }

        if (!enumValues.isEmpty()) {
            schema.setEnumValues(enumValues);
        }

        return schema;
    }

    private JsonSchemaObj buildSchemaForMap(TypeMirror paramTypeMirror, JsonSchemaProps schemaProps) {
        JsonSchemaObj mapSchema = new JsonSchemaObj();
        TypeMirror type = TypeUtils.getMapValueType(paramTypeMirror);

        Object additionalProps;
        if ("java.lang.Object".equals(TypeUtils.getTypeName(type))) {
            additionalProps = true; // allow any additional properties if value type is Object
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

        if (schemaProps.isNullable()) {
            schema.setNullable();
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
                if (TypeUtils.isEnum(typeMirror)) {
                    return buildSchemaForEnum(typeMirror, new JsonSchemaProps());
                } else {
                    // Custom object type
                    JsonSchemaObj objectSchema = new JsonSchemaObj();
                    List<VariableElement> fields = resolveObjectFields(typeMirror);
                    populateSchemaFromParams(objectSchema, fields);
                    return objectSchema;
                }
            }
        }

        // Handle primitive types
        JsonSchemaTypeInfo typeInfo = TypeMapper.toJsonSchemaTypeInfo(typeMirror);
        return new JsonSchemaBase(typeInfo);
    }

    @SuppressWarnings("unchecked")
    private List<VariableElement> resolveObjectFields(TypeMirror typeMirror) {
        TypeElement typeElement = toElement(typeMirror);
        return (List<VariableElement>) typeElement.getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .toList();
    }

    private static String serializeSchema(JsonSchemaBase schema) {
        try {
            return OBJECT_MAPPER.writeValueAsString(schema);
        } catch (Exception e) {
            throw new JsonSchemaSerializationException("Failed to serialize JSON schema", e);
        }
    }

    private TypeElement toElement(TypeMirror typeMirror) {
        return (TypeElement) ((DeclaredType) typeMirror).asElement();
    }

    public static String serializeSchemaObj(JsonSchemaObj schema) {
        return serializeSchema(schema);
    }
}
