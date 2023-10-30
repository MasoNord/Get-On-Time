package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.EntityType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.dao.UserDao;
import org.masonord.delivery.service.interfaces.UserServiceInterface;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements UserServiceInterface {

    @Autowired
    private UserDao userDao;

    private Argon2PasswordEncoder passwordEncoder;

    @Autowired
    private IdUtils idUtils;

    @Override
    public UserDto signup(UserDto userDto) {
        User user = userDao.findUserByEmail(userDto.getEmail());

        if (user == null) {
            user = new User()
                    .setEmail(userDto.getEmail())
                    .setRole(userDto.getRole())
                    .setPassword(passwordEncoder.encode(userDto.getPassword()))
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setId(idUtils.generateUuid());
            return UserMapper.toUserDto(userDao.creatUser(user));
        }

        throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail());
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userDao.findUserByEmail(email);
        if (user != null) {
            return UserMapper.toUserDto(user);
        }

        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public UserDto changePassword(String email, String oldPassword, String newPassword) {
        User user = userDao.findUserByEmail(email);

        if (user != null) {
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw exception(EntityType.USER, ExceptionType.WRONG_PASSWORD, "passwords are not match");
            }else {
                user.setPassword(newPassword);
                return UserMapper.toUserDto(userDao.updateUserProfile(user));
            }
        }

        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public UserDto updateProfile(String email, UserDto newUserProfile) {
        User user = userDao.findUserByEmail(email);

        if (user != null) {
            user
                    .setLastName(newUserProfile.getLastName())
                    .setFirstName(newUserProfile.getFirstName())
                    .setEmail(newUserProfile.getEmail());
            return UserMapper.toUserDto(userDao.updateUserProfile(user));
        }

        throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    private RuntimeException exception(EntityType entity, ExceptionType exception, String... args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }

    @Bean
    public Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 16384, 2);
    }
}
