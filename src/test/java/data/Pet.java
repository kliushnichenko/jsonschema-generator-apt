package data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pet {
    private String name;
    private int age;
    private Person owner;
}

@Getter
@Setter
class Person {
    private String name;
}