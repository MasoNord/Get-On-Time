package org.masonord.delivery.service.classes;

import org.masonord.delivery.config.ContextConfig;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.UserSignupRequest;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Customer;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.dao.CustomerDao;
import org.masonord.delivery.repository.dao.UserDao;
import org.masonord.delivery.service.interfaces.UserServiceInterface;
import org.masonord.delivery.util.FakeDataUtil;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("userService")
public class UserService implements UserServiceInterface {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Autowired
    private IdUtils idUtils;

    @Autowired
    private CourierService courierService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FakeDataUtil fakeData;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = ContextConfig.bCryptPasswordEncoder();

    @Override
    public UserDto signup(UserDto userDto, UserSignupRequest userSignupRequest) {
        User user = userDao.findUserByEmail(userDto.getEmail());

        if (user == null) {
            user = new User()
                .setEmail(userDto.getEmail())
                .setRole(userDto.getRole())
                .setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName());
            if (UserRoles.COURIER.equals(UserRoles.valueOf(userSignupRequest.getRole().toUpperCase()))) {
                CourierDto courierDto = new CourierDto()
                        .setEmail(userSignupRequest.getEmail())
                        .setLastName(userSignupRequest.getLastName())
                        .setFirstName(userSignupRequest.getFirstName())
                        .setWorkingHours(userSignupRequest.getWorkingHours())
                        .setTransport(CourierType.valueOf(userSignupRequest.getCourierType().toUpperCase()));

                courierService.addNewCourier(courierDto);
            }
            if (UserRoles.CUSTOMER.equals(UserRoles.valueOf(userSignupRequest.getRole().toUpperCase()))) {
                CustomerDto customerDto = new CustomerDto()
                        .setEmail(userSignupRequest.getEmail())
                        .setFirstName(userSignupRequest.getFirstName())
                        .setLastName(userSignupRequest.getLastName());

                customerService.addNewCustomer(customerDto);
            }
            return UserMapper.toUserDto(userDao.creatUser(user));
        }
        throw exception(ModelType.USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail());
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userDao.findUserByEmail(email);
        if (user != null) {
            return UserMapper.toUserDto(user);
        }

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }
    @Override
    public List<UserDto> getUsers() {
        List<UserDto> users = new LinkedList<>();
        List<User> userEntity = userDao.getAllUsers();
        for (int i = 0; i < userEntity.size(); i++) {
            users.add(UserMapper.toUserDto(userEntity.get(i)));
        }
        return users;
    }

    public List<UserDto> getUsers(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<UserDto> users = new LinkedList<>();
        List<User> userEntity = userDao.getAllUsers(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());
        for (int i = 0; i < userEntity.size(); i++) {
            users.add(UserMapper.toUserDto(userEntity.get(i)));
        }
        return users;
    }

    @Override
    public UserDto changePassword(String email, String oldPassword, String newPassword) {
        User user = userDao.findUserByEmail(email);

        if (user != null) {
            if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                throw exception(ModelType.USER, ExceptionType.WRONG_PASSWORD, "passwords are not match");
            }else {
                user.setPassword(newPassword);
                return UserMapper.toUserDto(userDao.updateUserProfile(user));
            }
        }

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
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

        throw exception(ModelType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
    }
    @Override
    public void createDummyUsers(int count) {
        for (int i = 0; i < count; i++) {
           UserSignupRequest userSignupRequest = new UserSignupRequest()
                   .setCourierType(fakeData.generateCourier().getValue().toUpperCase())
                   .setRole(fakeData.generateRole().getValue().toUpperCase())
                   .setPassword(fakeData.generatePassword())
                   .setFirstName(fakeData.generateFirstName())
                   .setLastName(fakeData.generateLastName())
                   .setEmail(fakeData.generateEmail())
                   .setWorkingHours("08:00-16:00");

           UserDto userDto = new UserDto()
                   .setPassword(userSignupRequest.getPassword())
                   .setEmail(userSignupRequest.getEmail())
                   .setRole(UserRoles.valueOf(userSignupRequest.getRole()))
                   .setLastName(userSignupRequest.getLastName())
                   .setFirstName(userSignupRequest.getFirstName());

           signup(userDto, userSignupRequest);
        }
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
