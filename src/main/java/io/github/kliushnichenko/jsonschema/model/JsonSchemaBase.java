package io.github.kliushnichenko.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumSet;

@Getter
@Setter
public class JsonSchemaBase {
    @JsonFormat(with = JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)
    protected EnumSet<JsonSchemaType> type;
    protected String description;
    protected String format; // for string type only
    @JsonProperty("default")
    protected Object defaultValue;

    public JsonSchemaBase(JsonSchemaType type) {
        this.type = EnumSet.of(type);
    }

    public JsonSchemaBase(JsonSchemaType type, String format) {
        this(type);
        this.format = format;
    }

    public JsonSchemaBase(JsonSchemaTypeInfo typeInfo) {
        this(typeInfo.getType());
        this.format = typeInfo.getFormat();
    }

    public void setNullable() {
        this.type.add(JsonSchemaType.NULL);
    }

}