package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.UserSignupRequest;
import org.masonord.delivery.dto.model.UserDto;

import java.util.List;

public interface UserServiceInterface {
    /**
     * Create a new user
     *
     * @param userDto
     * @return
     */
    UserDto signup(UserDto userDto, UserSignupRequest userSignupRequest);

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

    /**
     * Get all user records
     *
     * @return
     */

    List<UserDto> getUsers(OffsetBasedPageRequest offsetBasedPageRequest);

    /**
     * Create random users
     *
     * @param count - define how many users will be created
     */
    void createDummyUsers(int count);
}
