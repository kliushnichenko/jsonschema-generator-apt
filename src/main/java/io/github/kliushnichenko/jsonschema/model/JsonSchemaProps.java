package io.github.kliushnichenko.jsonschema.model;

import lombok.Getter;
import lombok.Setter;

import java.util.EnumSet;
import java.util.List;

/**
 * Represents a property within a JSON Schema.
 * Contains metadata and constraints for individual properties.
 */
@Getter
@Setter
public class JsonSchemaProps {
    private String name;
    private String description;
    private boolean required;
    private String defaultValue;
    private boolean nullable;
    private boolean additionalProperties;
    private EnumSet<JsonSchemaType> types;
    private List<String> allowableValues;

    public void applyTypes(String[] types) {
        this.types = EnumSet.noneOf(JsonSchemaType.class);
        for (String type : types) {
            this.types.add(JsonSchemaType.valueOf(type.toUpperCase()));
        }
    }
}
