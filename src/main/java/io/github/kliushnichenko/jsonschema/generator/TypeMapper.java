package io.github.kliushnichenko.jsonschema.generator;

import static io.github.kliushnichenko.jsonschema.generator.TypeUtils.isDeclaredType;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import io.github.kliushnichenko.jsonschema.model.JsonSchemaType;
import io.github.kliushnichenko.jsonschema.model.JsonSchemaTypeInfo;
import lombok.experimental.UtilityClass;

/**
 * Utility class responsible for mapping Java types to JSON Schema types.
 * Handles both primitive types and common wrapper classes.
 */
@UtilityClass
class TypeMapper {

    static JsonSchemaTypeInfo toJsonSchemaTypeInfo(TypeMirror typeMirror) {
        TypeKind typeKind = typeMirror.getKind();

        if (typeKind.isPrimitive()) {
            return mapPrimitiveTypeToJsonSchema(typeKind);
        }

        if (typeKind == TypeKind.ARRAY) {
            return JsonSchemaTypeInfo.of(JsonSchemaType.ARRAY);
        }

        if (isDeclaredType(typeKind)) {
            return mapDeclaredTypeToJsonSchema(typeMirror);
        }

        return new JsonSchemaTypeInfo(JsonSchemaType.OBJECT);
    }

    private static JsonSchemaTypeInfo mapPrimitiveTypeToJsonSchema(TypeKind typeKind) {
        return switch (typeKind) {
            case BOOLEAN -> JsonSchemaTypeInfo.of(JsonSchemaType.BOOLEAN);
            case BYTE, SHORT, INT, LONG -> JsonSchemaTypeInfo.of(JsonSchemaType.INTEGER);
            case FLOAT, DOUBLE -> JsonSchemaTypeInfo.of(JsonSchemaType.NUMBER);
            case CHAR -> JsonSchemaTypeInfo.of(JsonSchemaType.STRING);
            default -> throw new IllegalArgumentException("Unsupported primitive type: " + typeKind);
        };
    }

    private static JsonSchemaTypeInfo mapDeclaredTypeToJsonSchema(TypeMirror typeMirror) {
        String typeName = TypeUtils.getTypeName(typeMirror);

        return switch (typeName) {
            case "java.lang.String", "java.lang.Character" -> JsonSchemaTypeInfo.of(JsonSchemaType.STRING);
            case "java.lang.Boolean" -> JsonSchemaTypeInfo.of(JsonSchemaType.BOOLEAN);
            case "java.lang.Integer", "java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.math.BigInteger" ->
                    JsonSchemaTypeInfo.of(JsonSchemaType.INTEGER);
            case "java.lang.Double", "java.lang.Float", "java.math.BigDecimal" ->
                    JsonSchemaTypeInfo.of(JsonSchemaType.NUMBER);
            case "java.time.LocalDate", "java.time.LocalDateTime", "java.time.Instant", "java.time.ZonedDateTime",
                 "java.time.OffsetDateTime", "java.util.Date" ->
                    JsonSchemaTypeInfo.of(JsonSchemaType.STRING, "date-time");
            case "java.time.LocalTime" -> JsonSchemaTypeInfo.of(JsonSchemaType.STRING, "time");
            case "java.util.UUID" -> JsonSchemaTypeInfo.of(JsonSchemaType.STRING, "uuid");
            default -> JsonSchemaTypeInfo.of(JsonSchemaType.OBJECT);
        };
    }
}
