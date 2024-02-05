package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.RestaurantMapper;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.*;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.service.interfaces.RestaurantService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("RestaurantService")
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public RestaurantDto addNewRestaurant(RestaurantDto restaurantDto, String email) {
        Location location = locationService.addNewPlaceByName(restaurantDto.getLocation());

        if (Objects.isNull(restaurantRepository.getRestaurant(restaurantDto.getName()))) {

            Restaurant restaurant = new Restaurant()
                    .setLocation(location)
                    .setName(restaurantDto.getName())
                    .setUser(userRepository.findUserByEmail(email))
                    .setOrders(new HashSet<>())
                    .setReviews(new HashSet<>());


            Set<Menu> menus = new HashSet<>();
            restaurantRepository.createRestaurant(restaurant);
            for (MenuDto menu : restaurantDto.getMenus()) {
                IdUtils idUtils = new IdUtils();

                if (Objects.isNull(menuRepository.getMenuByName(menu.getName()))) {
                    Menu newMenu = new Menu()
                            .setId(idUtils.generateUuid())
                            .setRestaurant(restaurant)
                            .setMenuType(menu.getMenuType())
                            .setName(menu.getName())
                            .setDescription(menu.getDescription())
                            .setDu(DateUtils.todayToStr())
                            .setDc(DateUtils.todayToStr())
                            .setReviews(new HashSet<>());


                    menuRepository.addNewManu(newMenu);
                    Set<Dish> menuDishes = new HashSet<>();
                    for (DishDto dish : menu.getDishes()) {
                        idUtils = new IdUtils();
                        Dish exist = dishRepository.getDishByName(dish.getName());

                        if (Objects.isNull(exist)) {
                            Dish newDish = new Dish()
                                    .setMenu(newMenu)
                                    .setName(dish.getName())
                                    .setCost(dish.getCost())
                                    .setDescription(dish.getDescription())
                                    .setId(idUtils.generateUuid())
                                    .setReviews(new HashSet<>());
                            dishRepository.createDish(newDish);
                            menuDishes.add(newDish);
                        }
                            // add warn logging, the dish is already exists in the chosen menu
                    }
                    newMenu.setDishes(menuDishes);
                    menus.add(newMenu);
                    menuRepository.updateMenu(newMenu);
                }
                // logg warn, the menu is already exists;
            }
            restaurant.setMenus(menus);
            return RestaurantMapper.toRestaurantDto(restaurantRepository.updateRestaurant(restaurant));
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.DUPLICATE_ENTITY, email);
    }

    @Override
    public RestaurantService updateRestaurantProfile(RestaurantDto restaurantDto) {
        return null;
    }

    @Override
    public List<RestaurantDto> getAllRestaurants(int offset, int limit) {
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurants(offset, limit);

        return new ArrayList<>(restaurants
                .stream()
                .map(restaurant -> new ModelMapper().map(restaurant, RestaurantDto.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public List<OrderDto> getAllOrders(String restaurantName) {
        Restaurant restaurant = restaurantRepository.getRestaurant(restaurantName);
        if (restaurant != null) {
            return new ArrayList<>(restaurant.getOrders()
                    .stream()
                    .map(order -> new ModelMapper().map(order, OrderDto.class))
                    .collect(Collectors.toList())
            );
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND, restaurantName);
    }

    @Override
    public List<Restaurant> getMostPopularRestaurants() {
        return null;
    }

    @Override
    public List<RestaurantDto> getClosestRestaurants(String userEmail) {
        User user = userRepository.findUserByEmail(userEmail);
        List<Restaurant> restaurants = restaurantRepository.getAllRestaurants();
        List<RestaurantDto> closestRestaurants = new ArrayList<>();

        if (user.getLocation() != null) {
            for (Restaurant restaurant : restaurants) {
                double distance = LocationService.getDistanceFromLatLonInM(
                        user.getLocation().getLatitude(), user.getLocation().getLongitude(),
                        restaurant.getLocation().getLatitude(), restaurant.getLocation().getLongitude()
                );

                if (!Objects.equals(ModelType.COURIER.getValue(), user.getRole().getValue())) {
                    restaurant.setOrders(null);
                    if (distance <= 10000) {
                        closestRestaurants.add(RestaurantMapper.toRestaurantDto(restaurant));
                    }
                }else {
                    if (CourierType.WALK.equals(user.getTransport()) && distance <= 2500) {
                        closestRestaurants.add(RestaurantMapper.toRestaurantDto(restaurant));
                    } else if (CourierType.BICYCLE.equals(user.getTransport()) && distance <= 4000) {
                        closestRestaurants.add(RestaurantMapper.toRestaurantDto(restaurant));
                    } else if (CourierType.CAR.equals(user.getTransport()) && distance <= 10000) {
                        closestRestaurants.add(RestaurantMapper.toRestaurantDto(restaurant));
                    }
                }
            }
            return closestRestaurants;
        }
        throw exception(ModelType.USER, ExceptionType.LOCATION_NOT_SET);
    }

    @Override
    public Restaurant getRestaurantByName(String name) {
        return null;
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
