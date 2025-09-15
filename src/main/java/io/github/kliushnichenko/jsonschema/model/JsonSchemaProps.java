package io.github.kliushnichenko.jsonschema.model;

import lombok.Getter;
import lombok.Setter;

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
}
