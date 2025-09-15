package io.github.kliushnichenko.jsonschema.model;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface JsonSchemaAnnotationMapper<A extends Annotation> {

    JsonSchemaProps map(A annotation);
}
