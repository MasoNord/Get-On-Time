package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
    COURIER("COURIER"),
    USER("USER"),
    CUSTOMER("CUSTOMER"),
    ORDER("ORDER"),
    LOCATION("LOCATION"),
    COMPLETEDORDER("COMPLETEDORDER");

    private final String value;
}
