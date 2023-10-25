package org.masonord.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryType {
    UK("UK"),   // United Kingdom
    US("US"),   // United States
    NL("NL"),   // Netherlands
    DE("DE"),   // Germany
    FL("FL");   // Finland

    private final String value;
}
