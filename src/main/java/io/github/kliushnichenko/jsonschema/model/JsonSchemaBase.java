package io.github.kliushnichenko.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonSchemaBase {
    protected JsonSchemaType type;
    protected String description;
    protected String format; // for string type only
    @JsonProperty("default")
    protected Object defaultValue;

    public JsonSchemaBase(JsonSchemaType type) {
        this.type = type;
    }

    public JsonSchemaBase(JsonSchemaType type, String format) {
        this.type = type;
        this.format = format;
    }

    public JsonSchemaBase(JsonSchemaTypeInfo typeInfo) {
        this.type = typeInfo.getType();
        this.format = typeInfo.getFormat();
    }

}