package data;

import annotation.Arg;
import annotation.ExpectedSchema;
import lombok.NonNull;

import java.util.Map;
import java.util.UUID;

public class MapTypes {

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "mapping": {
                  "type": "object",
                  "description": "Mapping Rules",
                  "additionalProperties": {
                    "type": "integer"
                  }
                }
              },
              "required": [
                "mapping"
              ],
              "additionalProperties": false
            }""")
    void str2IntMap(@Arg(name = "mapping", description = "Mapping Rules")
                    @NonNull
                    Map<String, Integer>  str2IntMap) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "uuid2IntMap": {
                  "type": "object",
                  "additionalProperties": {
                    "type": "integer"
                  }
                }
              },
              "required": [
                "uuid2IntMap"
              ],
              "additionalProperties": false
            }""")
    void uuid2IntMap(Map<UUID, Integer> uuid2IntMap) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "str2PetMap": {
                  "type": "object",
                  "additionalProperties": {
                    "type": "object",
                    "properties": {
                      "name": {
                        "type": "string"
                      },
                      "age": {
                        "type": "integer"
                      },
                      "owner": {
                        "type": "object",
                        "properties": {
                          "name": {
                            "type": "string"
                          }
                        },
                        "required": [
                          "name"
                        ],
                        "additionalProperties": false
                      }
                    },
                    "required": [
                      "name",
                      "age",
                      "owner"
                    ],
                    "additionalProperties": false
                  }
                }
              },
              "required": [
                "str2PetMap"
              ],
              "additionalProperties": false
            }""")
    void str2PetMap(@NonNull Map<String, Pet> str2PetMap) {
    }
}
