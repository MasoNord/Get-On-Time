package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setRole(user.getRole());
    }
}
