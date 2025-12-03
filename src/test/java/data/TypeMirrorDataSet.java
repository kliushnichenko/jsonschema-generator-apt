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
                  "type": "number"
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
                    "type": "number"
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
                    "type": "number"
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
}
