package org.masonord.delivery.service;


import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.masonord.delivery.controller.v1.request.PasswordResetRequest;
import org.masonord.delivery.controller.v1.request.UpdateUserRequest;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.hibernate.HibernateUserRepository;
import org.masonord.delivery.service.classes.UserServiceImpl;
import org.masonord.delivery.service.interfaces.LocationService;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private HibernateUserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ExceptionHandler exceptionHandler;

    @Mock
    private LocationService locationService;

    private Faker faker;

    private String firstName;

    private String lastName;

    private String email;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.lorem().word();
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        });
    }


    @Test
    void it_should_register_a_user() {

        // given

        String password = faker.lorem().word();

        UserDto userDto = new UserDto()
                .setEmail(email)
                .setPassword(password)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setRole(UserRoles.ADMIN.getValue());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        User userExpected = new User()
                .setRole(UserRoles.ADMIN)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(password)
                .setEmail(email);

        given(userRepository.findUserByEmail(email)).willReturn(null);
        given(passwordEncoder.encode(password)).willReturn(password);
        given(userRepository.creatUser(any(User.class))).willReturn(userExpected);

        // when

        UserDto user = userService.signup(userDto);

        // then
        
        verify(userRepository).creatUser(userArgumentCaptor.capture());
        assertThat(user).isEqualToComparingOnlyGivenFields(userExpected);
        assertThat(userArgumentCaptor.getValue().getEmail()).isEqualTo(email);
        assertThat(userArgumentCaptor.getValue().getPassword()).isEqualTo(password);
    }

    @Test
    void is_should_register_a_courier() {

        // given
        String password = faker.lorem().word();

        UserDto userDto = new UserDto()
                .setEmail(email)
                .setPassword(password)
                .setRole(UserRoles.COURIER.getValue().toUpperCase())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setWorkingHours("08:00-16:00")
                .setTransport(CourierType.BICYCLE.getValue().toUpperCase());

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        User userExpected = new User()
                .setRole(UserRoles.COURIER)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(password)
                .setEmail(email)
                .setWorkingHours("08:00-16:00")
                .setTransport(CourierType.BICYCLE);

        given(userRepository.findUserByEmail(email)).willReturn(null);
        given(passwordEncoder.encode(password)).willReturn(password);
        given(userRepository.creatUser(any(User.class))).willReturn(userExpected);

        // when

        UserDto user = userService.signup(userDto);

        // then

        verify(userRepository).creatUser(userArgumentCaptor.capture());
        assertThat(user).isEqualToComparingOnlyGivenFields(userExpected);
        assertThat(userArgumentCaptor.getValue().getEmail()).isEqualTo(email);
        assertThat(userArgumentCaptor.getValue().getPassword()).isEqualTo(password);
    }

    @Test
    void is_should_not_register_a_courier_when_required_fields_are_empty() {

        // given

        UserDto userDto = new UserDto()
                .setEmail(email)
                .setRole(UserRoles.COURIER.getValue().toUpperCase());

        given(userRepository.findUserByEmail(email)).willReturn(null);
        given(exceptionHandler.throwException(ModelType.COURIER, ExceptionType.ENTITY_EXCEPTION)).willReturn(
                new ExceptionHandler.Exception("Wrong data set for a courier entity")
        );

        // when, then

        assertThatThrownBy(() -> userService.signup(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong data set for a courier entity");
    }

    @Test
    void it_should_not_register_a_user_when_it_exists() {

        // given

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        given(userRepository.findUserByEmail(email)).willReturn(new User());
        given(exceptionHandler.throwException(ModelType.USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail())).willReturn(
                new ExceptionHandler.DuplicateEntityException("An account already exists with this email")
        );

        // when, then

        assertThatThrownBy(() -> userService.signup(userDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("An account already exists with this email");
    }


    @Test
    void it_should_get_user() {

        // given
        String email = faker.lorem().word();
        String password = faker.lorem().word();

        User user = new User()
                .setEmail(email)
                .setPassword(password)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setRole(UserRoles.ADMIN);

        given(userRepository.findUserByEmail(email)).willReturn(user);

        // when

        UserDto userResult = userService.findUserByEmail(email);

        // then
        verify(userRepository).findUserByEmail(email);
        assertThat(userResult).isEqualToComparingFieldByField(UserMapper.toUserDto(user));
    }

    @Test
    void it_should_throw_exception_when_email_is_null() {

        // given
        given(exceptionHandler.throwException(ModelType.USER, ExceptionType.INVALID_ARGUMENT_EXCEPTION, "email")).willReturn(
                new ExceptionHandler.InvalidArgumentException("Null value was received")
        );

        // when, then

        assertThatThrownBy(() -> userService.findUserByEmail(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Null value was received");
    }

    @Test
    void it_should_reset_password() {
        String oldPassword = faker.howIMetYourMother().character();
        String newPassword = faker.random().hex();
        String activePassword = faker.random().hex();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setOldPassword(oldPassword);
        passwordResetRequest.setNewPassword(newPassword);

        User user = new User();
        user.setPassword(activePassword);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        given(userRepository.findUserByEmail(email)).willReturn(user);
        given(passwordEncoder.matches(oldPassword, activePassword)).willReturn(true);
        given(passwordEncoder.encode(newPassword)).willReturn(newPassword);
        given(userRepository.updateUserProfile(user)).willReturn(user);

        userService.changePassword(passwordResetRequest.oldPassword, passwordResetRequest.newPassword, email);

        verify(userRepository).updateUserProfile(userArgumentCaptor.capture());

        assertThat(user).isEqualTo(userArgumentCaptor.getValue());
    }

    @Test
    void it_should_throw_exception_when_old_password_and_new_password_is_not_equal() {
        given(userRepository.findUserByEmail(email)).willReturn(new User());
        given(passwordEncoder.matches(any(), any())).willReturn(false);
        given(exceptionHandler.throwException(ModelType.USER, ExceptionType.WRONG_PASSWORD, "")).willReturn(
                new ExceptionHandler.WrongPasswordException("The user's password is wrong")
        );

        assertThatThrownBy(() -> userService.changePassword("", "", email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The user's password is wrong");
    }

    @Test
    void it_should_update_user_address() {
        LocationDto locationDto = new LocationDto();

        User user = new User();
        String expectedResponse = "\"message\": \"User's current location has been successfully updated\"";

        given(userRepository.findUserByEmail(email)).willReturn(user);
        given(userRepository.updateUserProfile(user)).willReturn(user);
        given(locationService.addNewPlaceByName(locationDto)).willReturn(new Location());

        String response = userService.updateLocation(locationDto, email);

        verify(userRepository).findUserByEmail(email);
        verify(userRepository).updateUserProfile(user);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    void it_should_update_user_record() {

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();

        User user = new User();
        user.setRole(UserRoles.ADMIN);
        String expectedResponse = "\"message\": \"User has been successfully updated\"";

        given(userRepository.findUserByEmail(email)).willReturn(user);
        given(userRepository.updateUserProfile(user)).willReturn(user);

        String response  = userService.updateProfile(email, updateUserRequest);

        verify(userRepository).findUserByEmail(email);
        verify(userRepository).updateUserProfile(user);
        assertThat(response).isEqualTo(expectedResponse);

    }


}
