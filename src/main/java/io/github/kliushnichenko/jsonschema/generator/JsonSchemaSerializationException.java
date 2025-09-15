package io.github.kliushnichenko.jsonschema.generator;

/**
 * Exception thrown when JSON schema serialization fails.
 * This is a runtime exception that indicates a serious error
 * in the schema generation process that should not be recoverable.
 */
public class JsonSchemaSerializationException extends RuntimeException {

    public JsonSchemaSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
