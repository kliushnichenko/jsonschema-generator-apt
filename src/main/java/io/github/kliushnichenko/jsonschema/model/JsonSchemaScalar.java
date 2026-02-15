package io.github.kliushnichenko.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JsonSchemaScalar extends JsonSchemaBase {
    public JsonSchemaScalar(JsonSchemaType type) {
        super(type);
    }

    @JsonProperty("enum")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> enumValues;
}
