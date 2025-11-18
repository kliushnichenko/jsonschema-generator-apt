package data;

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
}
