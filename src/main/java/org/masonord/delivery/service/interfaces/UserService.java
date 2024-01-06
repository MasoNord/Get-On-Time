package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.UserDto;
import java.util.List;

public interface UserService {
    /**
     * Create a new user
     *
     * @param userDto
     * @return UserDto
     */

    UserDto signup(UserDto userDto);

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
     * @param email
     * @param newPassword
     * @return UserDto
     */

    UserDto changePassword(String email, String oldPassword, String newPassword);

    /**
     * Update user's personal data
     *
     * @param email
     * @param newUserProfile
     * @return UserDto
     */

    UserDto updateProfile(String email, UserDto newUserProfile);

    /**
     * Get all user records
     *
     * @return List<UserDto>
     */

    List<UserDto> getUsers(OffsetBasedPageRequest offsetBasedPageRequest);

    /**
     * Create random users
     *
     * @param count - define how many users will be created
     */

    void createDummyUsers(int count);
}
