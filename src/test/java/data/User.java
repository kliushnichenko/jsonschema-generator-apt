package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record User(
        @JsonProperty("first-name")
        String firstName,
        @Schema(name = "last_name", description = "User's last name")
        @JsonProperty("last-name")
        String lastName,
        String email) {
}
