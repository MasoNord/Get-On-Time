package org.masonord.delivery.service.classes;

import lombok.AllArgsConstructor;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.*;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.UserRepository;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.FakeDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("userService")
@AllArgsConstructor
public class UserServiceImpl implements org.masonord.delivery.service.interfaces.UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FakeDataUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           FakeDataUtil fakeData) {
        this.userRepository = userRepository;
    }


    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private ExceptionHandler exceptionHandler;
//
//    @Autowired
//    private IdUtils idUtils;
//
//    @Autowired
//    private CourierServiceImpl courierService;
//
//    @Autowired
//    private CustomerServiceImpl customerService;

    @Autowired
    private FakeDataUtil fakeData;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto signup(UserDto userDto) {
        User user = userRepository.findUserByEmail(userDto.getEmail());

        if (user == null) {
            if (Objects.equals(userDto.getRole().toString(), "COURIER")) {
                if (!Objects.isNull(userDto.getTransport()) && !Objects.isNull(userDto.getWorkingHours())) {
                    user = new User()
                            .setEmail(userDto.getEmail())
                            .setRole(UserRoles.valueOf(userDto.getRole().toUpperCase()))
                            .setWorkingHours(userDto.getWorkingHours())
                            .setTransport(CourierType.valueOf(userDto.getTransport().toUpperCase()))
                            .setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()))
                            .setFirstName(userDto.getFirstName())
                            .setLastName(userDto.getLastName())
                            .setDc(DateUtils.todayToStr())
                            .setDu(DateUtils.todayToStr());
                }else {
                    throw exception(ModelType.COURIER, ExceptionType.ENTITY_EXCEPTION, "");
                }
            }else {
                user = new User()
                        .setEmail(userDto.getEmail())
                        .setRole(UserRoles.valueOf(userDto.getRole().toUpperCase()))
                        .setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()))
                        .setFirstName(userDto.getFirstName())
                        .setLastName(userDto.getLastName())
                        .setDc(DateUtils.todayToStr())
                        .setDu(DateUtils.todayToStr());
            }
            return UserMapper.toUserDto(userRepository.creatUser(user));
        }
        throw exception(ModelType.USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findUserByEmail(email);

        if (user != null) {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().getValue()));

            return buildUserForAuthentication(user, authorities);
        }

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return UserMapper.toUserDto(user);
        }

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public List<UserDto> getUsers(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<UserDto> users = new LinkedList<>();
        List<User> userEntity = userRepository.getAllUsers(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());
        for (int i = 0; i < userEntity.size(); i++) {
            users.add(UserMapper.toUserDto(userEntity.get(i)));
        }
        return users;
    }

    @Override
    public UserDto changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmail(email);

        if (user != null) {
            if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                throw exception(ModelType.USER, ExceptionType.WRONG_PASSWORD, "passwords are not match");
            }else {
                user.setPassword(newPassword);
                return UserMapper.toUserDto(userRepository.updateUserProfile(user));
            }
        }

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public UserDto updateProfile(String email, UserDto newUserProfile) {
        User user = userRepository.findUserByEmail(email);

        if (user != null) {
            user
                    .setLastName(newUserProfile.getLastName())
                    .setFirstName(newUserProfile.getFirstName())
                    .setEmail(newUserProfile.getEmail());
            return UserMapper.toUserDto(userRepository.updateUserProfile(user));
        }

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public void createDummyUsers(int count) {
        for (int i = 0; i < count; i++) {
           UserDto userDto = new UserDto()
                   .setPassword(fakeData.generatePassword())
                   .setEmail(fakeData.generateEmail())
                   .setRole(fakeData.generateRole().getValue().toUpperCase())
                   .setLastName(fakeData.generateLastName())
                   .setFirstName(fakeData.generateFirstName())
                   .setWorkingHours("08:00-16:00")
                   .setTransport(fakeData.generateCourier().getValue());
           signup(userDto);
        }
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }

    private UserDetails buildUserForAuthentication(User user, Collection<SimpleGrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
