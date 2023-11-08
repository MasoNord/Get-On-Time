package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum StatusCode {
    OK(200),
    CREATE(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    TOO_MANY_REQUESTS(429);

    private final int code;
}
