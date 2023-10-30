package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.UserDto;

public interface UserServiceInterface {
    /**
     * Create a new user
     *
     * @param userDto
     * @return
     */
    UserDto signup(UserDto userDto);

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */
    UserDto findUserByEmail(String email);

    /**
     * Change user's password
     * @param email
     * @param newPassword
     * @return
     */
    UserDto changePassword(String email, String oldPassword, String newPassword);

    /**
     * Update user's personal data
     *
     * @param email
     * @param newUserProfile
     * @return
     */
    UserDto updateProfile(String email, UserDto newUserProfile);
}