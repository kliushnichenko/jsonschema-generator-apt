package data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumSet;

@Getter
@Setter
public class EntityWithEnum {

    private Status status;

    private EnumSet<Status> listOfStatuses;

    @Schema(allowableValues = {"Active", "Inactive", "Pending"})
    private Status statusOverriddenOnField;

    private Size size;

    enum Status {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    @Schema(allowableValues = {"L", "XXL", "XXXL"})
    enum Size {
        SMALL("L"),
        MEDIUM("XXL"),
        LARGE("XXXL");

        @Getter
        private final String code;

        Size(String code) {
            this.code = code;
        }
    }
}
