package tests;

import annotation.Arg;
import data.*;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaAnnotationMapper;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaProps;
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

    @Test
    void verifyIgnoreTypesAreSkipped() {
        new ProcessorRunner(IgnoreTypes.class, MAPPERS);
    }

    @Test
    void generate_byTypeMirror() {
        new ProcessorRunner(TypeMirrorDataSet.class, ProcessorRunner.Type.TYPE_MIRROR);
    }
}
