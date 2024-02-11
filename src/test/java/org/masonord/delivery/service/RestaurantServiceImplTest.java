package org.masonord.delivery.service;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.masonord.delivery.dto.mapper.RestaurantMapper;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.*;
import org.masonord.delivery.service.classes.RestaurantServiceImpl;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.service.interfaces.UserService;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Mock
    private LocationService locationService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ExceptionHandler exceptionHandler;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    void it_should_get_all_restaurants() {

        // given
        Integer offset = faker.number().randomDigitNotZero();
        Integer limit = faker.number().randomDigitNotZero() + offset;

        Restaurant restaurant = new Restaurant();

        List<Restaurant> restaurants = Stream.generate(() -> restaurant)
                .limit(faker.number().randomDigitNotZero())
                .toList();

        RestaurantDto restaurantDto = new RestaurantDto();

        given(restaurantRepository.getAllRestaurants(offset, limit)).willReturn(restaurants);

        List<RestaurantDto> restaurantDtoList= restaurantService.getAllRestaurants(offset, limit);

        assertThat(restaurantDtoList.size()).isEqualTo(restaurants.size());
    }

    @Test
    void it_should_add_new_restaurant() {

        // given

        String restaurantName = faker.lorem().word();
        String menuName = faker.lorem().word();
        String dishName = faker.lorem().word();
        String email = faker.lorem().word();

        Set<DishDto> dishes = new HashSet<>();
        Set<MenuDto> menus = new HashSet<>();
        menus.add(new MenuDto().setDishes(dishes).setName(menuName));

        RestaurantDto restaurantDto = new RestaurantDto()
                .setName(restaurantName)
                .setMenus(menus);

        ArgumentCaptor<Restaurant> restaurantArgumentCaptor = ArgumentCaptor.forClass(Restaurant.class);
        RestaurantDto restaurantExpected = new RestaurantDto();
        Restaurant restaurant = new Restaurant();
        Menu menu = new Menu();
        Dish dish = new Dish();

        given(restaurantRepository.getRestaurant(restaurantName)).willReturn(null);
        given(restaurantRepository.createRestaurant(restaurant)).willReturn(restaurant);
        given(menuRepository.getMenuByName(menu.getName())).willReturn(null);
        given(menuRepository.addNewManu(menu)).willReturn(menu);
        given(dishRepository.getDishByName(dishName)).willReturn(null);
        given(dishRepository.createDish(dish)).willReturn(dish);
        given(menuRepository.updateMenu(menu)).willReturn(menu);
        given(restaurantRepository.updateRestaurant(restaurant)).willReturn(restaurant);

        // when
        RestaurantDto restaurantDtoExpected = restaurantService.addNewRestaurant(restaurantDto);

        //then
        verify(restaurantRepository.createRestaurant(restaurantArgumentCaptor.capture()));
        assertThat(RestaurantMapper.toRestaurantDto(restaurant)).isEqualToComparingFieldByField(restaurantDtoExpected);
    }

    @Test
    void it_should_get_closest_restaurants_for_non_courier_role() {
        String email = faker.lorem().word();
        String name = faker.name().name();
        User user = new User()
                .setLocation(new Location())
                .setRole(UserRoles.CUSTOMER);

        Restaurant restaurant = new Restaurant()
                .setLocation(new Location())
                .setMenus(new HashSet<>())
                .setReviews(new HashSet<>())
                .setName(name);


        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        List<RestaurantDto> response;

        given(userRepository.findUserByEmail(email)).willReturn(user);
        given(restaurantRepository.getAllRestaurants()).willReturn(restaurants);
        try(MockedStatic<LocationService> mockedStatic = Mockito.mockStatic(LocationService.class)) {
            mockedStatic.when(() -> LocationService.getDistanceFromLatLonInM (
                   anyDouble(), anyDouble(),
                    anyDouble(), anyDouble())
            ).thenReturn(8564.55);


            response = restaurantService.getClosestRestaurants(email);
            mockedStatic.verify(() -> LocationService.getDistanceFromLatLonInM (
                    anyDouble(), anyDouble(),
                    anyDouble(), anyDouble())
            );

        }

        assertThat(response.get(0))
                .usingRecursiveComparison()
                .isEqualTo(RestaurantMapper.toRestaurantDto(restaurant));
    }

    @Test
    void it_should_not_add_restaurant_if_it_is_far_away_from_courier() {
        String email = faker.lorem().word();
        String name = faker.name().name();
        User user = new User()
                .setLocation(new Location())
                .setRole(UserRoles.COURIER)
                .setTransport(CourierType.WALK);

        Restaurant restaurant = new Restaurant()
                .setLocation(new Location())
                .setMenus(new HashSet<>())
                .setReviews(new HashSet<>())
                .setName(name);


        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        List<RestaurantDto> response;

        given(userRepository.findUserByEmail(email)).willReturn(user);
        given(restaurantRepository.getAllRestaurants()).willReturn(restaurants);
        try(MockedStatic<LocationService> mockedStatic = Mockito.mockStatic(LocationService.class)) {
            mockedStatic.when(() -> LocationService.getDistanceFromLatLonInM (
                    anyDouble(), anyDouble(),
                    anyDouble(), anyDouble())
            ).thenReturn(8564.55);


            response = restaurantService.getClosestRestaurants(email);
            mockedStatic.verify(() -> LocationService.getDistanceFromLatLonInM (
                    anyDouble(), anyDouble(),
                    anyDouble(), anyDouble())
            );

        }

        assertThat(response.size()).isEqualTo(0);
    }

    @Test
    void it_should_throw_exception_if_user_location_is_null() {
        String email = faker.lorem().word();
        User user = new User();

        given(userRepository.findUserByEmail(email)).willReturn(user);
        given(exceptionHandler.throwException(ModelType.USER, ExceptionType.LOCATION_NOT_SET)).willReturn(
                new ExceptionHandler.LocationNotSetException("The user didn't provide its location data.")
        );

        assertThatThrownBy(() -> restaurantService.getClosestRestaurants(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The user didn't provide its location data.");

    }
}
