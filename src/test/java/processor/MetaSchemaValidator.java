package processor;

import com.networknt.schema.*;
import com.networknt.schema.Error;
import com.networknt.schema.dialect.Dialects;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.List;

class MetaSchemaValidator {

    private final Schema schema;

    MetaSchemaValidator() {
        SchemaRegistry schemaRegistry = SchemaRegistry.withDialect(Dialects.getDraft202012());

        this.schema = schemaRegistry.getSchema(SchemaLocation.of(Dialects.getDraft202012().getId()));

    }

    public void validate(String generatedJsonSchema, String method, Messager messager) {
        List<Error> errors = schema.validate(generatedJsonSchema,
                InputFormat.JSON,
                executionContext -> executionContext.executionConfig(config -> {
                    config.formatAssertionsEnabled(true);
                    config.failFast(true);
                }));

        if (!errors.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    """
                            Invalid JSON Schema generated.
                            Method name: %s
                            Errors:
                            %s
                            """.formatted(method, errors)
            );
        }
    }
}
