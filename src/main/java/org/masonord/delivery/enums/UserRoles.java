package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    ADMIN("admin"),
    CUSTOMER("customer"),
    COURIER("courier");

    private final String value;
}
