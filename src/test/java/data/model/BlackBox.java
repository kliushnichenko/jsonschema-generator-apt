package data.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BlackBox(
        @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE)
        Object anObject,
        List<Object> listOfObjects,
        @Schema(types = {"object", "array"}, additionalProperties = Schema.AdditionalPropertiesValue.TRUE)
        JsonNode jsonNode
){}
