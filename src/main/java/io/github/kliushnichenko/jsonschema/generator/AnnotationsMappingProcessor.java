package io.github.kliushnichenko.jsonschema.generator;

import io.github.kliushnichenko.jsonschema.model.JsonSchemaProps;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaAnnotationMapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Processor for mapping annotations on method parameters to JSON schema properties.
 */
class AnnotationsMappingProcessor {

    private final Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> mappers;
    private final Map<String, Class<? extends Annotation>> annotationNameToClass;
    private final Set<String> annotations;

    AnnotationsMappingProcessor(Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> mappers) {
        this.mappers = mappers;
        this.annotationNameToClass = mappers.keySet()
                .stream()
                .collect(Collectors.toMap(Class::getCanonicalName, clazz -> clazz));
        this.annotations = annotationNameToClass.keySet();
    }

    public JsonSchemaProps evalJsonSchemaProps(VariableElement parameter) {
        String paramName = parameter.getSimpleName().toString();

        JsonSchemaProps schemaProps = new JsonSchemaProps();
        schemaProps.setName(paramName);
        schemaProps.setRequired(true);

        for (AnnotationMirror annotationMirror : parameter.getAnnotationMirrors()) {
            DeclaredType annotationType = annotationMirror.getAnnotationType();
            String annotationClassName = annotationType.toString();

            if (annotations.contains(annotationClassName)) {
                var annotationClass = annotationNameToClass.get(annotationClassName);
                var annotation = parameter.getAnnotation(annotationClass);

                @SuppressWarnings("unchecked")
                var mapper = (JsonSchemaAnnotationMapper<Annotation>) mappers.get(annotationClass);
                schemaProps = mapper.map(annotation);

                if (schemaProps.getName() == null || schemaProps.getName().isEmpty()) {
                    schemaProps.setName(paramName); // restore name from parameter if default annotation value is ""
                }
                break;
            }
        }

        return schemaProps;
    }
}
