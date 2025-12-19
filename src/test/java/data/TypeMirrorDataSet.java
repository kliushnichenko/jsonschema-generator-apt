package data;

import annotation.Container;
import annotation.ExpectedSchemaForTypeMirror;

/**
 * @author kliushnichenko
 */
public class TypeMirrorDataSet {

    @ExpectedSchemaForTypeMirror(value = """
            {
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
            }""",
            type = Pet.class
    )
    public void petType() {
    }

    @ExpectedSchemaForTypeMirror(value = """
            {
              "type": "array",
              "items": {
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
              }
            }""",
            type = Pet.class,
            container = Container.LIST
    )
    public void petsArrayType() {
    }

    @ExpectedSchemaForTypeMirror(value = """
            {
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
            }""",
            type = Pet.class,
            container = Container.MAP
    )
    public void petsMapType() {
    }

    @ExpectedSchemaForTypeMirror(value = """
            {
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
            }""",
            type = User.class
    )
    public void userWithJacksonAnnotation() {
    }
}
