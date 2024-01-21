package org.masonord.delivery.repository;

import org.masonord.delivery.model.User;
import java.util.List;

public interface UserRep {
    User creatUser(User user);

    User findUserByEmail(String email);

    User updateUserProfile(User user);

    List<User> getAllUsers();

    List<User> getAllUsers(int offset, int limit);
}
