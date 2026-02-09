package data;

import annotation.Arg;
import annotation.ExpectedSchema;
import data.model.Pet;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CollectionTypes {

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "vector": {
                  "type": "array",
                  "items": {
                    "type": "integer"
                  }
                }
              },
              "required": [
                "vector"
              ],
              "additionalProperties": false
            }""")
    void arrayOfPrimitives(int[] vector) {}

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "vector": {
                  "type": "array",
                  "description": "vector description",
                  "items": {
                    "type": "integer"
                  }
                }
              },
              "required": [
                "vector"
              ],
              "additionalProperties": false
            }""")
    void listOfInteger(@Arg(description = "vector description") @NonNull List<Integer> vector) {}

    @ExpectedSchema("""
            {
              "type": "object",
              "properties": {
                "pets": {
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
                        "type": [
                          "object",
                          "null"
                        ],
                        "description": "Person details",
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
                }
              },
              "required": [
                "pets"
              ],
              "additionalProperties": false
            }""")
    void listOfPets(ArrayList<Pet> pets) {}
}
