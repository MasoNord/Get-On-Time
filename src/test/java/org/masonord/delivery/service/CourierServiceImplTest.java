package org.masonord.delivery.service;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.masonord.delivery.dto.mapper.CourierMapper;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.repository.CompletedOrderRepository;
import org.masonord.delivery.repository.OrderRepository;
import org.masonord.delivery.repository.UserRepository;
import org.masonord.delivery.service.classes.CourierServiceImpl;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.util.IdUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
public class CourierServiceImplTest {
    @InjectMocks
    private CourierServiceImpl courierService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private CompletedOrderRepository completedOrderRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private IdUtils idUtils;

    @Mock
    private ExceptionHandler exceptionHandler;

    private Faker faker;

    private String email;

    private String orderId;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        email = faker.lorem().word();
        orderId = idUtils.generateUuid();
    }

    @Test
    void it_should_find_customer_by_email() {
        User courier = new User()
                .setRole(UserRoles.COURIER)
                .setFirstName(faker.lorem().word())
                .setLastName(faker.lorem().word())
                .setTransport(CourierType.WALK)
                .setWorkingHours("08:00-16:00")
                .setRides(new HashSet<Order>());

        given(userRepository.findUserByEmail(email)).willReturn(courier);

        CourierDto response = courierService.findCourierByEmail(email);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(CourierMapper.toCourierDto(courier));

    }

    @Test
    void it_should_throw_exception_if_courier_is_not_found() {
        given(exceptionHandler.throwException(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email))
                .willReturn(new ExceptionHandler.EntityNotFoundException("The courier was not found by the email"));
        given(userRepository.findUserByEmail(email)).willReturn(null);

        assertThatThrownBy(() -> courierService.findCourierByEmail(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The courier was not found by the email");
    }
}
