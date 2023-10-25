package org.masonord.delivery.repository;

import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.model.User;

public interface UserRepository {
    User addUser(UserDto userDto);

    User findByEmail(String email);
}
