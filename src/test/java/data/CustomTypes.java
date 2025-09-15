package data;

import annotation.Arg;
import annotation.ExpectedSchema;
import lombok.NonNull;

public class CustomTypes {

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "pet": {
                  "type": "object",
                  "description": "Pet Description",
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
              },
              "required": [
                "pet"
              ],
              "additionalProperties": false
            }""")
    void customTypePet(@Arg(name = "pet", description = "Pet Description") @NonNull Pet myPet) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "pet": {
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
                },
                "locationId": {
                  "type": "integer"
                }
              },
              "required": [
                "pet",
                "locationId"
              ],
              "additionalProperties": false
            }""")
    void customTypePetAndPrimitiveArgument(@NonNull Pet pet, @NonNull int locationId) {
    }

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "pet": {
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
                "pet",
                "owner"
              ],
              "additionalProperties": false
            }""")
    void twoCustomTypesPetAndPerson(Pet pet, Person owner) {
    }
}
