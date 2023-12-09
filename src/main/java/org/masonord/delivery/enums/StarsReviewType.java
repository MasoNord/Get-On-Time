package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StarsReviewType {
    VERY_BAD(0),
    BAD(1),
    MEDIUM(2),
    GOOD(3),
    VERY_GOOD(4),
    EXCELLENT(5);

    private final int value;
}
