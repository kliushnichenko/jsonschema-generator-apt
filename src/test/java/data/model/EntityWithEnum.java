package data.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityWithEnum {

    private Status status;

    enum Status {
        ACTIVE,
        INACTIVE,
        PENDING
    }
}
