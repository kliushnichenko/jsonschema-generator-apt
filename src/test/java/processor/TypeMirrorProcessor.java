package processor;

import annotation.Container;
import annotation.ExpectedSchemaForTypeMirror;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kliushnichenko.jsonschema.generator.JsonSchemaGenerator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("annotation.ExpectedSchemaForTypeMirror")
public class TypeMirrorProcessor extends AbstractProcessor {

    private Messager messager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TypeMirrorProcessor() {
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

        Set<? extends Element> methods = roundEnv.getElementsAnnotatedWith(ExpectedSchemaForTypeMirror.class);

        messager.printMessage(Diagnostic.Kind.WARNING, "Processing " + methods.size() + " methods");

        for (Element method : methods) {
            var annotation = method.getAnnotation(ExpectedSchemaForTypeMirror.class);
            String expectedJsonSchema = annotation.value();
            Container container = annotation.container();
            TypeMirror typeMirror = null;
            try {
                processingEnv.getElementUtils()
                        .getTypeElement(annotation.type().getCanonicalName()).asType();
            } catch (MirroredTypeException mte) {
                typeMirror = resolveTypeMirror(mte, container);
            }
            String generatedJsonSchema = new JsonSchemaGenerator()
                    .generate(typeMirror);
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

    private TypeMirror resolveTypeMirror(MirroredTypeException mte, Container container) {
        TypeMirror typeMirror;
        if (Container.LIST == container) {
            typeMirror = processingEnv.getTypeUtils()
                    .getDeclaredType(
                            processingEnv.getElementUtils().getTypeElement("java.util.List"),
                            mte.getTypeMirror()
                    );
        } else if (Container.MAP == container) {
            var strTypeMirror = processingEnv.getElementUtils()
                    .getTypeElement("java.lang.String")
                    .asType();
            typeMirror = processingEnv.getTypeUtils()
                    .getDeclaredType(
                            processingEnv.getElementUtils().getTypeElement("java.util.Map"),
                            strTypeMirror,
                            mte.getTypeMirror()
                    );
        } else {
            typeMirror = mte.getTypeMirror();
        }
        return typeMirror;
    }
}
