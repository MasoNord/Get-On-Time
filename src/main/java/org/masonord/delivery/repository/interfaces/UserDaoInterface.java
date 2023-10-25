package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.model.User;

public interface UserDaoInterface {
    User creatUser(User user);

    User findUserById(String email);
}
