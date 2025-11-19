package data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pet {
    private String name;
    private int age;
    private Person owner;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private double weight;
}

@Getter
@Setter
class Person {
    private String name;
}
