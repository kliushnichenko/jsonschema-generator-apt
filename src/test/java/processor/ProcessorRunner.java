package processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.JavaFileObjects;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaAnnotationMapper;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.google.testing.compile.Compiler.javac;

public class ProcessorRunner {

    private final Compilation compilation;

    public ProcessorRunner(Class<?> adapter) {
        this(adapter, Map.of());
    }

    public ProcessorRunner(Class<?> adapter, Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> argResolvers) {
        MethodsProcessor processor = new MethodsProcessor(argResolvers);

        List<JavaFileObject> sources = List.of(getSource(adapter));
        this.compilation = javac()
                .withProcessors(processor)
                .compile(sources);

        CompilationSubject.assertThat(compilation).succeeded();
    }

    private JavaFileObject getSource(Class<?> clazz) {
        String resourcePath = clazz.getName().replace('.', '/') + ".java";
        Path path = Path.of(System.getProperty("user.dir") + File.separator + "src")
                .resolve("test")
                .resolve("java")
                .resolve(resourcePath);
        try {
            return JavaFileObjects.forSourceString(clazz.getName(), Files.readString(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProcessorRunner logAllCompilerMessages() {
        compilation.diagnostics().forEach(diagnostic ->
                System.err.println(diagnostic.getKind() + ": " + diagnostic.getMessage(null))
        );
        return this;
    }
}
