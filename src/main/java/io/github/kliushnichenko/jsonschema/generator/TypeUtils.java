package io.github.kliushnichenko.jsonschema.generator;

import lombok.experimental.UtilityClass;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

/**
 * Utility class for type-related operations, such as determining if a type is a collection,
 * extracting component types, and checking for built-in types.
 */
@UtilityClass
public class TypeUtils {

    private static final Set<String> BUILT_IN_TYPES = Set.of(
            // Wrapper types
            "java.lang.String",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            // Big number types
            "java.math.BigDecimal",
            "java.math.BigInteger",
            // Date/time types
            "java.time.LocalDate",
            "java.time.LocalDateTime",
            "java.time.LocalTime",
            "java.time.Instant",
            "java.time.ZonedDateTime",
            "java.time.OffsetDateTime",
            "java.util.Date",
            // Special types
            "java.util.UUID"
    );

    static boolean isDeclaredType(TypeKind typeKind) {
        return typeKind == TypeKind.DECLARED;
    }

    /**
     * Get the element type for arrays and collections
     */
    public static TypeMirror getCollectionComponentType(TypeMirror typeMirror) {
        // Handle arrays
        if (TypeKind.ARRAY == typeMirror.getKind()) {
            ArrayType arrayType = (ArrayType) typeMirror;
            return arrayType.getComponentType();
        }

        // Handle collections
        if (isDeclaredType(typeMirror.getKind()) && typeMirror instanceof DeclaredType declaredType) {
            var typeArguments = declaredType.getTypeArguments();
            if (!typeArguments.isEmpty()) {
                return typeArguments.get(0);
            }
        }

        throw new IllegalArgumentException("The type is neither array nor collection: " + typeMirror);
    }

    /**
     * Get the value type for map types
     */
    public static TypeMirror getMapValueType(TypeMirror typeMirror) {
        if (typeMirror instanceof DeclaredType declaredType) {
            var typeArguments = declaredType.getTypeArguments();
            if (typeArguments.size() >= 2) {
                return typeArguments.get(1); // Second type argument (V in Map<K, V>)
            } else {
                throw new IllegalArgumentException("The map type has no value type: " + typeMirror);
            }
        } else {
            throw new IllegalArgumentException("The type is not a map: " + typeMirror);
        }
    }

    /**
     * Check if a type is an array or collection type
     */
    public static boolean isIterableType(TypeMirror typeMirror) {
        TypeKind typeKind = typeMirror.getKind();
        if (typeKind == TypeKind.ARRAY) {
            return true;
        }

        if (isDeclaredType(typeKind)) {
            String typeName = getTypeName(typeMirror);
            return isCollectionType(typeName);
        }

        return false;
    }

    /**
     * Check if a type name corresponds to a collection type
     */
    public static boolean isCollectionType(String typeName) {
        return typeName.startsWith("java.util.List") ||
               typeName.startsWith("java.util.Set") ||
               typeName.startsWith("java.util.Collection") ||
               typeName.startsWith("java.util.ArrayList") ||
               typeName.startsWith("java.util.LinkedList") ||
               typeName.startsWith("java.util.HashSet") ||
               typeName.startsWith("java.util.LinkedHashSet") ||
               typeName.startsWith("java.util.TreeSet");
    }

    /**
     * Check if a type name corresponds to a map type
     */
    public static boolean isMapType(TypeMirror typeMirror) {
        String typeName = getTypeName(typeMirror);

        return typeName.startsWith("java.util.Map") ||
               typeName.startsWith("java.util.HashMap") ||
               typeName.startsWith("java.util.LinkedHashMap") ||
               typeName.startsWith("java.util.TreeMap") ||
               typeName.startsWith("java.util.ConcurrentHashMap");
    }

    /**
     * Check if a type is a custom object type (not a built-in Java type)
     */
    static boolean isCustomObjectType(TypeMirror typeMirror) {
        TypeKind typeKind = typeMirror.getKind();

        if (!isDeclaredType(typeKind)) {
            return false;
        }

        return !isBuiltInType(typeMirror);
    }

    public static String getTypeName(TypeMirror typeMirror) {
        if (typeMirror instanceof DeclaredType declaredType) {
            TypeElement typeElement = (TypeElement) declaredType.asElement();
            return typeElement.getQualifiedName().toString();
        }
        return typeMirror.toString();
    }

    static boolean isBuiltInType(TypeMirror typeMirror) {
        String typeName = getTypeName(typeMirror);
        return BUILT_IN_TYPES.contains(typeName);
    }
}
