package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.UpdateUserRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.model.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    /**
     * Create a new user
     *
     * @param userDto
     * @return UserDto
     */

    UserDto signup(UserDto userDto);

    UserDto fetchUser();

    User getUser();

    /**
     * Search an existing user
     *
     * @param email
     * @return UserDto
     */

    UserDto findUserByEmail(String email);

    /**
     * Change user's password
     *
     * @param newPassword
     * @return UserDto
     */

    String changePassword(String oldPassword, String newPassword);

    /**
     * Update user's personal data
     *
     * @param updateUserRequest
     * @return String
     */

    String updateProfile(UpdateUserRequest updateUserRequest);

    /**
     * Get all user records
     *
     * @return List<UserDto>
     */

    List<UserDto> getUsers(int offset, int limit);

    /**
     * Update user's current location
     *
     * @param locationDto
     * @return
     */

    String updateLocation(LocationDto locationDto);
}
