package org.masonord.delivery.controller.v1.api;

import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.UserPasswordChangeRequest;
import org.masonord.delivery.controller.v1.request.UserSignupRequest;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.service.classes.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController("UserController")
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        UserDto userDto = new UserDto()
                .setEmail(userSignupRequest.getEmail())
                .setRole(UserRoles.valueOf(userSignupRequest.getRole().toUpperCase()))
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setPassword(userSignupRequest.getPassword());

        return Response.ok().setPayload(userService.signup(userDto, userSignupRequest));
    }

    @GetMapping("/{email}")
    public Response getUser(@PathVariable String email) {
        return Response.ok().setPayload(userService.findUserByEmail(email));
    }
    @GetMapping
    public Response getUsers() {
        return Response.ok().setPayload(userService.getUsers());
    }

    @PutMapping("/password/{email}")
    public Response changePassword(@RequestBody @Valid UserPasswordChangeRequest userPasswordChangeRequest, @PathVariable String email) {
        return Response.ok().setPayload(userService.changePassword(userPasswordChangeRequest.oldPassword, userPasswordChangeRequest.newPassword, email));
    }

}
