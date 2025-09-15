package tests;

import annotation.Arg;
import data.CollectionTypes;
import data.CustomTypes;
import data.MapTypes;
import data.ScalarBuiltInTypes;
import io.github.kliushnichenko.jsonschema.generator.JsonSchemaGenerator;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaProps;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaAnnotationMapper;
import org.junit.jupiter.api.Test;
import processor.ProcessorRunner;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Test for JsonSchemaGenerator to verify actual JSON schema generation.
 */
class JsonSchemaGeneratorTest {

    private static final JsonSchemaAnnotationMapper<Arg> ARG_MAPPER = (Arg annotation) -> {
            JsonSchemaProps schemaProps = new JsonSchemaProps();
            schemaProps.setName(annotation.name());
            schemaProps.setDescription(annotation.description());
            schemaProps.setRequired(annotation.required());
            schemaProps.setDefaultValue(annotation.defaultValue());
            return schemaProps;
    };

    private static final Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> MAPPERS = Map.of(
            Arg.class, ARG_MAPPER
    );

    @Test
    void verifyPrimitiveTypesGeneration() {
        new ProcessorRunner(ScalarBuiltInTypes.class, MAPPERS);
    }

    @Test
    void verifyCustomTypesGeneration() {
        new ProcessorRunner(CustomTypes.class, MAPPERS);
    }

    @Test
    void verifyCollectionTypesGeneration() {
        new ProcessorRunner(CollectionTypes.class, MAPPERS);
    }

    @Test
    void verifyMapTypesGeneration() {
        new ProcessorRunner(MapTypes.class, MAPPERS);
    }
}