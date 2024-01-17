package org.masonord.delivery.controller.v1.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.enums.UserRoles;
import org.springframework.http.HttpHeaders;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.UserPasswordChangeRequest;
import org.masonord.delivery.controller.v1.request.UserSignupRequest;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
@Tag(name = "user", description = "the user API")
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

    @Operation(summary = "Get a user by his email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User has been found",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class))
        }),
        @ApiResponse(responseCode = "404", description = "Use has not been found",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation  = Response.class))
        }),
    })
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

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                // TODO: replace this piece of code with a dedicated method for generating tokens to get rid of redundancy
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("SecreteKeyTOGenJWTs".getBytes()); // TODO: make retrieving secret key from a property file
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                UserDto user = userService.findUserByEmail(email);
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRole())
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception exception) {
                // TODO: add an error log here
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
