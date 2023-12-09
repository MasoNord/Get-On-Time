package org.masonord.delivery.controller.v1.api;

import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.UserPasswordChangeRequest;
import org.masonord.delivery.controller.v1.request.UserSignupRequest;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.interfaces.UserService;
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
                .setRole(userSignupRequest.getRole().toUpperCase())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setPassword(userSignupRequest.getPassword())
                .setTransport(userSignupRequest.getTransport())
                .setWorkingHours(userSignupRequest.getWorkingHours());

        return Response.ok().setPayload(userService.signup(userDto));
    }

    @GetMapping("/{email}")
    public Response getUser(@PathVariable String email) {
        return Response.ok().setPayload(userService.findUserByEmail(email));
    }

    @GetMapping
    public Response getUsers(@RequestParam(defaultValue = "0", required = false) int offset,
                             @RequestParam(defaultValue = "1", required = false) int limit) {
        return Response.ok().setPayload(userService.getUsers(new OffsetBasedPageRequest(offset, limit)));
    }

    @PutMapping("/password/{email}")
    public Response changePassword(@RequestBody @Valid UserPasswordChangeRequest userPasswordChangeRequest, @PathVariable String email) {
        return Response.ok().setPayload(userService.changePassword(userPasswordChangeRequest.oldPassword, userPasswordChangeRequest.newPassword, email));
    }

    @PostMapping(value = "/random", params = {"count"})
    public Response createDummyUsers(@RequestParam(value = "count", defaultValue = "1") int count) {
        userService.createDummyUsers(count);
        return Response.ok().setPayload("dummy users has been successfully created");
    }

}
