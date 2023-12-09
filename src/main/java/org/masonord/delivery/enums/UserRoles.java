package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    ADMIN("admin"),
    CUSTOMER("customer"),
    COURIER("courier"),
    OWNER("owner"),
    RESTAURANT_WORKER("restaurant_worker");
    private final String value;
}
