package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModelType {
    COURIER("courier"),
    CUSTOMER("customer"),
    USER("user"),
    RESTAURANT("restaurant"),
    MENU("menu"),
    DISH("dish"),
    REVIEW("REVIEW"),
    ORDER("order"),
    LOCATION("location"),
    COMPLETEDORDER("completedorder");

    private final String value;
}
