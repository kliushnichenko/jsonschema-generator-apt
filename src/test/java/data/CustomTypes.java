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
                    },
                    "weight": {
                      "type": "number",
                      "description": "Weight of the pet in kilograms"
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
                    },
                    "weight": {
                      "type": "number",
                      "description": "Weight of the pet in kilograms"
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
                    },
                    "weight": {
                      "type": "number",
                      "description": "Weight of the pet in kilograms"
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

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "user": {
                  "type": "object",
                  "properties": {
                    "first-name": {
                      "type": "string"
                    },
                    "last-name": {
                      "type": "string",
                      "description": "User's last name"
                    },
                    "email": {
                      "type": "string"
                    }
                  },
                  "required": [
                    "first-name",
                    "last-name",
                    "email"
                  ],
                  "additionalProperties": false
                }
              },
              "required": [
                "user"
              ],
              "additionalProperties": false
            }""")
    void userWithJsonPropertyAnnotations(User user) {
    }
}
