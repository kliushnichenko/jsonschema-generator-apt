package io.github.kliushnichenko.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JsonSchemaObj extends JsonSchemaBase {

    private Map<String, Object> properties;
    private List<String> required;
    private Object additionalProperties = false;
    @JsonProperty("$defs")
    private Map<String, Object> defs;

    public JsonSchemaObj() {
        super(JsonSchemaType.OBJECT);
    }

}