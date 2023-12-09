package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusType {

    PENDING("pending"),
    COOKING("cooking"),
    COOKED("cooked"),
    DRIVE("drive");

    private final String value;
}
