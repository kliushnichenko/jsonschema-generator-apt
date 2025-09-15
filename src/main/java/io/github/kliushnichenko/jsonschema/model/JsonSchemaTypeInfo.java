package io.github.kliushnichenko.jsonschema.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JsonSchemaTypeInfo {
    
    private final JsonSchemaType type;
    private final String format;
    
    public JsonSchemaTypeInfo(JsonSchemaType type) {
        this.type = type;
        this.format = null;
    }
    
    public static JsonSchemaTypeInfo of(JsonSchemaType type) {
        return new JsonSchemaTypeInfo(type);
    }
    
    public static JsonSchemaTypeInfo of(JsonSchemaType type, String format) {
        return new JsonSchemaTypeInfo(type, format);
    }
}
