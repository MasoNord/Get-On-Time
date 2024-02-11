package org.masonord.delivery.service;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.masonord.delivery.dto.mapper.CustomerMapper;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.UserRepository;
import org.masonord.delivery.service.classes.CustomerServiceImpl;
import org.masonord.delivery.service.classes.LocationServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    UserRepository userRepository;

    @Mock
    LocationServiceImpl locationService;

    @Mock
    ExceptionHandler exceptionHandler;

    private Faker faker;

    private String email;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        email = faker.lorem().word();
    }

    @Test
    void it_should_get_customer_by_email() {
        User customer = new User();
        customer.setEmail(email);
        customer.setRole(UserRoles.CUSTOMER);
        customer.setLastName(faker.lorem().word());
        customer.setFirstName(faker.lorem().word());
        customer.setOrders(new HashSet<>());

        given(userRepository.findUserByEmail(email)).willReturn(customer);

        CustomerDto result = customerService.findCustomerByEmail(email);

        assertThat(result).usingRecursiveComparison().isEqualTo(CustomerMapper.toCustomerDto(customer));
    }

    @Test
    void it_should_throw_exception_when_email_is_null() {
        given(exceptionHandler.throwException(ModelType.CUSTOMER, ExceptionType.INVALID_ARGUMENT_EXCEPTION)).willReturn(
                new ExceptionHandler.InvalidArgumentException("Invalid arguments")
        );

        assertThatThrownBy(() -> customerService.findCustomerByEmail(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid arguments");
    }

    @Test
    void it_should_throw_exception_if_user_is_not_customer() {
        User customer = new User()
                .setRole(UserRoles.COURIER);

        given(userRepository.findUserByEmail(email)).willReturn(customer);
        given(exceptionHandler.throwException(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email)).willReturn(
                new ExceptionHandler.EntityNotFoundException("The customer was not found by the given email")
        );

        assertThatThrownBy(() -> customerService.findCustomerByEmail(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The customer was not found by the given email");
    }

    @Test
    void it_should_get_all_active_orders() {
        Order order = new Order()
                .setCustomer(new User())
                .setOrderItems(new OrderItem())
                .setCourier(new User())
                .setRestaurant(new Restaurant());

        Set<Order> orders = Stream.generate(() -> order)
                .limit(faker.number().randomDigitNotZero())
                .collect(Collectors.toSet());

        User customer = new User()
                .setOrders(orders);

        OrderDto response = new OrderDto();

        given(userRepository.findUserByEmail(email)).willReturn(customer);

        List<OrderDto> orderResponseList = customerService.getActiveOrders(email);

        assertThat(orderResponseList.size()).isEqualTo(orders.size());
        orderResponseList.forEach(orderDto -> assertThat(orderDto).usingRecursiveComparison().isEqualTo(response));
    }

    @Test
    void it_should_not_get_orders_when_email_is_null() {
        given(exceptionHandler.throwException(ModelType.COURIER, ExceptionType.INVALID_ARGUMENT_EXCEPTION))
                .willReturn(new ExceptionHandler.InvalidArgumentException("Invalid arguments"));

        assertThatThrownBy(() -> customerService.getActiveOrders(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid arguments");
    }

    @Test
    void it_should_not_get_orders_when_customer_is_not_found() {
        given(exceptionHandler.throwException(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email))
                .willReturn(new ExceptionHandler.EntityNotFoundException("The customer was not found by the given email"));

        given(userRepository.findUserByEmail(email)).willReturn(null);

        assertThatThrownBy(() -> customerService.getActiveOrders(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The customer was not found by the given email");
    }

}
