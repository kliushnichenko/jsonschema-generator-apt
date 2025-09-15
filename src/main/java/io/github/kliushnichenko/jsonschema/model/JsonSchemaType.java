package io.github.kliushnichenko.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of all possible JSON Schema types according to the JSON Schema
 * specification.
 * Each enum value contains the string representation used in JSON Schema
 * documents.
 */
@Getter
@RequiredArgsConstructor
public enum JsonSchemaType {

    STRING("string"),
    NUMBER("number"),
    INTEGER("integer"),
    BOOLEAN("boolean"),
    OBJECT("object"),
    ARRAY("array"),
    NULL("null");

    @JsonValue
    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
