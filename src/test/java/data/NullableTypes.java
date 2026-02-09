package data;

import annotation.ExpectedSchemaForTypeMirror;
import data.model.EntityWithNullable;

public class NullableTypes {

    @ExpectedSchemaForTypeMirror(value = """
            {
              "type": "object",
              "properties": {
                "counter": {
                  "type": [
                    "integer",
                    "null"
                  ]
                },
                "listItems": {
                  "type": [
                    "array",
                    "null"
                  ],
                  "items": {
                    "type": "string"
                  }
                },
                "mapItems": {
                  "type": [
                    "object",
                    "null"
                  ],
                  "additionalProperties": true
                }
              },
              "required": [
                "counter",
                "listItems",
                "mapItems"
              ],
              "additionalProperties": false
            }""",
            type = EntityWithNullable.class
    )
    public void nullable() {
    }
}
