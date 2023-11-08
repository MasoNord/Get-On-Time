package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelType {
    COURIER("courier"),
    USER("user"),
    CUSTOMER("customer"),
    ORDER("order"),
    LOCATION("location"),
    COMPLETEDORDER("completedorder");

    private final String value;
}
