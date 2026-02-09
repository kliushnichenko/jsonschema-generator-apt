package data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

@Getter
@Setter
public class Pet {
    private String name;
    private int age;

    @Schema(nullable = true)
    private Person owner;

    @Schema(requiredMode = NOT_REQUIRED, description = "Weight of the pet in kilograms")
    private double weight;
}

