package processor;

import annotation.ExpectedSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kliushnichenko.jsonschema.generator.JsonSchemaGenerator;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaAnnotationMapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("annotation.ExpectedSchema")
public class MethodsProcessor extends AbstractProcessor {

    private Messager messager;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> mappers = Map.of();
    private Set<String> ignoreTypes = Set.of();

    public MethodsProcessor() {
    }

    public MethodsProcessor(Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> mappers,
                            Set<String> ignoreTypes) {
        this.mappers = mappers;
        this.ignoreTypes = ignoreTypes;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        Set<? extends Element> methods = roundEnv.getElementsAnnotatedWith(ExpectedSchema.class);

        messager.printMessage(Diagnostic.Kind.WARNING, "Processing " + methods.size() + " methods");

        for (Element method : methods) {
            String expectedJsonSchema = method.getAnnotation(ExpectedSchema.class).value();
            String generatedJsonSchema = new JsonSchemaGenerator(mappers, ignoreTypes)
                    .generate((ExecutableElement) method);
            try {
                JsonNode generatedJson = objectMapper.readTree(generatedJsonSchema);
                generatedJsonSchema = objectMapper.setDefaultPrettyPrinter(new CanonicalPrettyPrinter())
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(generatedJson);

                if (!expectedJsonSchema.equals(generatedJsonSchema)) {
                    messager.printMessage(Diagnostic.Kind.ERROR,
                            """
                                    Generated Schema doesn't match expected Schema.
                                    Method name: %s
                                                                        
                                    ====== Expected:
                                    %s
                                                                        
                                    ====== Generated:
                                    %s
                                    """.formatted(method.getSimpleName(), expectedJsonSchema, generatedJsonSchema)
                    );
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
