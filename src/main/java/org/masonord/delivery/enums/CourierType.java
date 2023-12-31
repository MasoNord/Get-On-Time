package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourierType {
    WALK("walk"),
    BICYCLE("bicycle"),
    CAR("car");

    private final String value;
}
