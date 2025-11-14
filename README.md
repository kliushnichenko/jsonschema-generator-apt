# JSON Schema Generator APT

A build-time Java library for generating JSON Schema. Unlike many runtime solutions, this library analyzes code during
compilation to produce JSON schemas, so its primary purpose is APT(Annotation Processing Tools) processors.

## Usage

To start using the library, include it as a dependency in your Maven project:

```xml
<dependency>
    <groupId>io.github.kliushnichenko</groupId>
    <artifactId>jsonschema-generator-apt</artifactId>
    <version>${version}</version>
</dependency>
```

Generate JsonSchema using `JsonSchemaGenerator` class:

Primary use-case for the time being is to generate schema for method arguments:

```java
import io.github.kliushnichenko.jsonschema.generator.JsonSchemaGenerator;

JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator();
String schema = schemaGenerator.generate(method)); // method is ExecutableElement instance
```

For example, for the method declaration like:

```java
int sum(int a, int b) {}
```

The generated schema will look like:

```json
{
  "type": "object",
  "properties": {
    "a": {
      "type": "integer"
    },
    "b": {
      "type": "integer"
    }
  },
  "required": [
    "a",
    "b"
  ],
  "additionalProperties": false
}
```

## Annotation Mappers

The library provides a mechanism to customize how specific annotations are translated into JSON Schema properties.
Let's say you have a custom annotation `@Arg` that you want to affect the resulting JSON Schema.

```java
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface Arg {
    String name() default "";
    String description() default "";
    boolean required() default true;
    String defaultValue() default "";
}
```

Apply this annotation to method parameters:
```java
int sum(@Arg(name = "firstNumber", description = "The first number to add") int a,
        @Arg(name = "secondNumber", description = "The second number to add") int b);
```

Implement corresponding mapper and pass it as an argument to `JsonSchemaGenerator`:
```java
import io.github.kliushnichenko.jsonschema.generator.JsonSchemaGenerator;
import io.github.kliushnichenko.jsonschema.generator.JsonSchemaAnnotationMapper;
import io.github.kliushnichenko.jsonschema.generator.model.JsonSchemaProps;

class MyGenerator {

    private static final JsonSchemaAnnotationMapper<Arg> ARG_MAPPER = (Arg annotation) -> {
        JsonSchemaProps schemaProps = new JsonSchemaProps();
        schemaProps.setName(annotation.name());
        schemaProps.setDescription(annotation.description());
        schemaProps.setRequired(annotation.required());
        return schemaProps;
    };

    private static final Map<Class<? extends Annotation>, JsonSchemaAnnotationMapper<?>> MAPPERS = Map.of(
            Arg.class, ARG_MAPPER
    );

    public static void main(String[] args) {
        JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(MAPPERS);
        String schema = schemaGenerator.generate(method);
    }
}
```

So, the generated schema will look like:

```json
{
  "type": "object",
  "properties": {
    "firstNumber": {
      "type": "integer",
      "description": "The first number to add"
    },
    "secondNumber": {
      "type": "integer",
      "description": "The second number to add"
    }
  },
  "required": [
    "firstNumber",
    "secondNumber"
  ],
  "additionalProperties": false
}
```

## Ignore arguments by type

You can configure the `JsonSchemaGenerator` to ignore specific arguments by type during schema generation. 
For example, if you want to ignore parameters of type `HttpServletRequest`, 
you can do so by passing a set of types to ignore when creating the generator:

```java

...

Set<String> typesToIgnore = Set.of("javax.servlet.http.HttpServletRequest");

JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator();
String schema = schemaGenerator.generate(method, typesToIgnore);
```
