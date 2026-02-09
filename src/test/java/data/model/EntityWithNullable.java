package data.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

public record EntityWithNullable(
        @Schema(nullable = true)
        Integer counter,

        @Schema(nullable = true)
        List<String> listItems,

        @Schema(nullable = true)
        Map<String, Object> mapItems) {
}
