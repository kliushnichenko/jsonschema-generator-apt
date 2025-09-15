package io.github.kliushnichenko.jsonschema.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonSchemaArray extends JsonSchemaBase {
    private JsonSchemaBase items;

    public JsonSchemaArray() {
        super(JsonSchemaType.ARRAY);
    }
}