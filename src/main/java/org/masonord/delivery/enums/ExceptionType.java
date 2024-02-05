package org.masonord.delivery.enums;
import lombok.AllArgsConstructor; import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    NOT_UUID_FORMAT("not.uuid"),
    DUPLICATE_ENTITY("duplicate"),
    ENTITY_EXCEPTION("exception"),
    WRONG_PASSWORD("wrong.password"),
    RANGE_NOT_SATISFIABLE("not.satisfiable"),
    CONFLICT_EXCEPTION("conflict"),
    LOCATION_NOT_SET("not.location"),
    INVALID_ARGUMENT_EXCEPTION("invalid.arguments");

    private final String value;
}
