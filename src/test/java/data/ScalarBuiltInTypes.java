package data;

import annotation.Arg;
import annotation.ExpectedSchema;
import lombok.NonNull;
import org.junit.Ignore;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.UUID;

public class ScalarBuiltInTypes {

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "boolArg": {
                  "type": "boolean"
                },
                "byteArg": {
                  "type": "integer"
                },
                "shortArg": {
                  "type": "integer"
                },
                "intArg": {
                  "type": "integer"
                },
                "longArg": {
                  "type": "integer"
                },
                "floatArg": {
                  "type": "number"
                },
                "doubleArg": {
                  "type": "number"
                }
              },
              "required": [
                "boolArg",
                "byteArg",
                "shortArg",
                "intArg",
                "longArg",
                "floatArg",
                "doubleArg"
              ],
              "additionalProperties": false
            }""")
    void allPrimitives(@NonNull boolean boolArg,
                       byte byteArg,
                       short shortArg,
                       int intArg,
                       long longArg,
                       float floatArg,
                       double doubleArg) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "user_id": {
                  "type": "integer",
                  "description": "A user unique identifier"
                },
                "isActive": {
                  "type": "boolean"
                }
              },
              "required": [
                "isActive"
              ],
              "additionalProperties": false
            }""")
    void argResolverExample(
            @Arg(name = "user_id", description = "A user unique identifier", required = false) long userId,
            boolean isActive) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "byte_arg": {
                  "type": "integer",
                  "description": "Byte Arg Description"
                },
                "shortArg": {
                  "type": "integer"
                },
                "integerArg": {
                  "type": "integer"
                },
                "longArg": {
                  "type": "integer"
                },
                "floatArg": {
                  "type": "number"
                },
                "doubleArg": {
                  "type": "number"
                },
                "bigIntegerArg": {
                  "type": "integer"
                },
                "bigDecimalArg": {
                  "type": "number"
                }
              },
              "required": [
                "shortArg",
                "integerArg",
                "longArg",
                "floatArg",
                "doubleArg",
                "bigIntegerArg",
                "bigDecimalArg"
              ],
              "additionalProperties": false
            }""")
    void declaredNumericTypes(
            @Arg(name = "byte_arg", description = "Byte Arg Description", required = false) Byte byteArg,
            Short shortArg,
            Integer integerArg,
            Long longArg,
            Float floatArg,
            Double doubleArg,
            @NonNull BigInteger bigIntegerArg,
            BigDecimal bigDecimalArg) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "stringArg": {
                  "type": "string"
                },
                "characterArg": {
                  "type": "string"
                }
              },
              "required": [
                "stringArg",
                "characterArg"
              ],
              "additionalProperties": false
            }""")
    void declaredStringTypes(@NonNull String stringArg, Character characterArg) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "localDateArg": {
                  "type": "string",
                  "format": "date-time"
                },
                "localDateTimeArg": {
                  "type": "string",
                  "format": "date-time"
                },
                "localTimeArg": {
                  "type": "string",
                  "format": "time"
                },
                "instantArg": {
                  "type": "string",
                  "format": "date-time"
                },
                "zonedDateTimeArg": {
                  "type": "string",
                  "format": "date-time"
                },
                "offsetDateTimeArg": {
                  "type": "string",
                  "format": "date-time"
                },
                "dateArg": {
                  "type": "string",
                  "format": "date-time"
                }
              },
              "required": [
                "localDateArg",
                "localDateTimeArg",
                "localTimeArg",
                "instantArg",
                "zonedDateTimeArg",
                "offsetDateTimeArg",
                "dateArg"
              ],
              "additionalProperties": false
            }""")
    void declaredDateTimeTypes(
            @NonNull LocalDate localDateArg,
            LocalDateTime localDateTimeArg,
            LocalTime localTimeArg,
            Instant instantArg,
            ZonedDateTime zonedDateTimeArg,
            OffsetDateTime offsetDateTimeArg,
            Date dateArg) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "booleanArg": {
                  "type": "boolean"
                },
                "uuidArg": {
                  "type": "string",
                  "format": "uuid"
                }
              },
              "required": [
                "booleanArg",
                "uuidArg"
              ],
              "additionalProperties": false
            }""")
    void declaredSpecialTypes(@NonNull Boolean booleanArg, @NonNull @Arg UUID uuidArg) {
    }
}
