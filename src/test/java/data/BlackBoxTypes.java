package data;

import annotation.ExpectedSchemaForTypeMirror;
import data.model.BlackBox;

public class BlackBoxTypes {

    @ExpectedSchemaForTypeMirror(value = """
            {
              "type": "object",
              "properties": {
                "anObject": {
                  "type": "object",
                  "properties": {},
                  "additionalProperties": true
                },
                "listOfObjects": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "properties": {},
                    "additionalProperties": false
                  }
                },
                "jsonNode": {
                  "type": [
                    "object",
                    "array"
                  ],
                  "properties": {},
                  "additionalProperties": true
                }
              },
              "required": [
                "anObject",
                "listOfObjects",
                "jsonNode"
              ],
              "additionalProperties": false
            }""",
            type = BlackBox.class
    )
    public void blackboxTypes() {
    }
}
