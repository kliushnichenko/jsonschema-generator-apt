package data;

import annotation.ExpectedSchema;

import java.io.File;

/**
 * @author kliushnichenko
 */
public class IgnoreTypes {

    @ExpectedSchema("""
            {
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
            }""")
    void ignoreFileType(String name, File file) {
    }
}
