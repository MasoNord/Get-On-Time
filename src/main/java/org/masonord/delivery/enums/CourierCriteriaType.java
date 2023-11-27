package org.masonord.delivery.enums;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourierCriteriaType {
    CAR(1),
    BICYCLE(2),
    WALK(3);

    private final int value;
}