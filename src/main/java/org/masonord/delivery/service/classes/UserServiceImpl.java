package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.UpdateUserRequest;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.*;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.UserRepository;
import org.masonord.delivery.service.interfaces.CourierService;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.util.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements org.masonord.delivery.service.interfaces.UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ExceptionHandler exceptionHandler;
    private final LocationService locationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           ExceptionHandler exceptionHandler,
                           LocationService locationService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = passwordEncoder;
        this.exceptionHandler = exceptionHandler;
        this.locationService = locationService;
    }

    @Override
    public UserDto signup(UserDto userDto) {
        User user = userRepository.findUserByEmail(userDto.getEmail());

        if (Objects.isNull(user)) {
            user = new User();
            if (Objects.equals(userDto.getRole().toUpperCase(), "COURIER") &&
                    !Objects.isNull(userDto.getTransport()) &&
                    !Objects.isNull(userDto.getWorkingHours())) {
                user
                        .setWorkingHours(userDto.getWorkingHours())
                        .setRides(new HashSet<>())
                        .setTransport(CourierType.valueOf(userDto.getTransport().toUpperCase()));
            }else {
                throw exception(ModelType.COURIER, ExceptionType.ENTITY_EXCEPTION);
            }
            if (Objects.equals(userDto.getRole().toUpperCase(), "OWNER")) {
                user.setRestaurants(new HashSet<>());
            }
            user
                    .setEmail(userDto.getEmail())
                    .setRole(UserRoles.valueOf(userDto.getRole().toUpperCase()))
                    .setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()))
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setDc(DateUtils.todayToStr())
                    .setDu(DateUtils.todayToStr());
            return UserMapper.toUserDto(userRepository.creatUser(user));
        }
        throw exception(ModelType.USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail());

    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        if (!Objects.isNull(email)) {
            User user = userRepository.findUserByEmail(email);

            if (user != null) {
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(user.getRole().getValue()));

                return buildUserForAuthentication(user, authorities);
            }

            throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
        }

        throw exception(ModelType.USER, ExceptionType.INVALID_ARGUMENT_EXCEPTION, "email");
    }

    @Override
    public UserDto findUserByEmail(String email) {
        if (!Objects.isNull(email)) {
            User user = userRepository.findUserByEmail(email);
            if (user != null) {
                return UserMapper.toUserDto(user);
            }

            throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
        }

        throw exception(ModelType.USER, ExceptionType.INVALID_ARGUMENT_EXCEPTION, "email");
    }

    @Override
    public List<UserDto> getUsers(int offset, int limit) {
        return new ArrayList<>(
                userRepository.getAllUsers(offset, limit)
                .stream()
                .map(user -> new ModelMapper().map(user, UserDto.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public String changePassword(String oldPassword, String newPassword, String email) {
        if (!Objects.isNull(email)) {
            User user = userRepository.findUserByEmail(email);

            if (user != null) {
                if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                    user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    return "The user's password has been successfully updated ";
                }
                throw exception(ModelType.USER, ExceptionType.WRONG_PASSWORD, "");
            }
            throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
        }
        throw exception(ModelType.USER, ExceptionType.INVALID_ARGUMENT_EXCEPTION, "email");
    }



    @Override
    public String updateProfile(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            if (Objects.equals(ModelType.COURIER.getValue(), user.getRole().getValue())) {
                user.setTransport(!Objects.isNull(updateUserRequest.getCourierType())
                        ? updateUserRequest.getCourierType()
                        : user.getTransport()
                );
            }
            user
                    .setLastName(updateUserRequest.getLastName())
                    .setFirstName(updateUserRequest.getFirstName())
                    .setEmail(updateUserRequest.getEmail());

            userRepository.updateUserProfile(user);
            return "The user's record has been successfully updated";
        }
        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public String updateLocation(LocationDto locationDto, String email) {
        if (!Objects.isNull(email)) {
            User user = userRepository.findUserByEmail(email);

            if (!Objects.isNull(user)) {
                Location location = locationService.addNewPlaceByName(locationDto);
                user.setLocation(location);
                userRepository.updateUserProfile(user);
                return "\"message\": \"The user's current location has been successfully updated\"";
            }
            throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
        }
        throw exception(ModelType.USER, ExceptionType.INVALID_ARGUMENT_EXCEPTION, "email");
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return exceptionHandler.throwException(entity, exception, args);
    }

    private UserDetails buildUserForAuthentication(User user, Collection<SimpleGrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
