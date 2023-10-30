package org.masonord.delivery.util;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class IdUtils {
    private final UUID uuid = UUID.randomUUID();
    private final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public String generateUuid() {
        return uuid.toString();
    }

    public boolean validateUuid(String id) {
        return UUID_REGEX.matcher(id).matches();
    }
//        return UUID.fromString(id).toString().equals(id);
}
