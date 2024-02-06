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
import org.masonord.delivery.controller.v1.request.*;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.model.User;
import org.springframework.http.HttpHeaders;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
@Tag(name = "user", description = "the user API")
@RestController("UserController")
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "")
    })

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        UserDto userDto = new UserDto()
                .setEmail(userSignupRequest.getEmail())
                .setRole(userSignupRequest.getRole().getValue())
                .setFirstName(userSignupRequest.getFirstName())
                .setLastName(userSignupRequest.getLastName())
                .setPassword(userSignupRequest.getPassword())
                .setTransport(userSignupRequest.getTransport().getValue())
                .setWorkingHours(userSignupRequest.getWorkingHours());

        return ResponseEntity.ok().body(userService.signup(userDto));
    }

    @Operation(summary = "Get a user by his email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User has been found",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class))
        }),
        @ApiResponse(responseCode = "404", description = "Use has not been found",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation  = ResponseEntity.class))
        }),
    })
    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.findUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(defaultValue = "0", required = false) int offset,
                             @RequestParam(defaultValue = "1", required = false) int limit) {
        return ResponseEntity.ok().body(userService.getUsers(offset, limit));
    }

    @PutMapping("/password/update")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid UserPasswordChangeRequest userPasswordChangeRequest,
                                         HttpServletRequest request) {
        return ResponseEntity.ok().body(
                userService.changePassword(
                    userPasswordChangeRequest.oldPassword,
                    userPasswordChangeRequest.newPassword,
                    request.getUserPrincipal().getName()
                )
        );
    }

    @PutMapping(value = "/location/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCurrentLocation(@RequestBody @Valid LocationAddRequest locationAddRequest, HttpServletRequest request) {
        LocationDto locationDto = new LocationDto()
                .setZip(locationAddRequest.getZip())
                .setNumber((locationAddRequest.getNumber()))
                .setCity(locationAddRequest.getCity())
                .setCountry(locationAddRequest.getCountry())
                .setStreet(locationAddRequest.getStreet());

        return ResponseEntity.ok().body(userService.updateLocation(locationDto, request.getUserPrincipal().getName()));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUserRecord(@RequestBody @Valid UpdateUserRequest updateUserRequest, HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.updateProfile(request.getUserPrincipal().getName(), updateUserRequest));
    }


    // TODO:

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
