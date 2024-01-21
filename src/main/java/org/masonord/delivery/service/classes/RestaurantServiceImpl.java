package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.RestaurantMapper;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.OrderStatusType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.*;
import org.masonord.delivery.repository.hibernate.*;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.service.interfaces.RestaurantService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("RestaurantService")
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    LocationService locationService;

    @Autowired
    UserRep userRep;

    @Autowired
    OrderRep orderRep;

    @Autowired
    MenuRep menuRep;

    @Autowired
    DishRep dishRep;

    @Autowired
    RestaurantRep restaurantRep;

    @Override
    public RestaurantDto addNewRestaurant(RestaurantDto restaurantDto, String email) {
        Location location = locationService.addNewPlaceByName(restaurantDto.getLocation());

        if (restaurantRep.getRestaurant(restaurantDto.getName()) == null) {

            Restaurant restaurant = new Restaurant()
                    .setLocation(location)
                    .setName(restaurantDto.getName())
                    .setUser(userRep.findUserByEmail(email)) // change the method parameters from user's email to the actual user
                    .setOrders(new HashSet<>())
                    .setReviews(new HashSet<>());


            Set<Menu> menus = new HashSet<>();
            restaurantRep.createRestaurant(restaurant);
            for (MenuDto menu : restaurantDto.getMenus()) {
                IdUtils idUtils = new IdUtils();

                if (menuRep.getMenuByName(menu.getName()) == null) {
                    Menu newMenu = new Menu()
                            .setId(idUtils.generateUuid())
                            .setRestaurant(restaurant)
                            .setMenuType(menu.getMenuType())
                            .setName(menu.getName())
                            .setDescription(menu.getDescription())
                            .setDu(DateUtils.todayToStr())
                            .setDc(DateUtils.todayToStr())
                            .setReviews(new HashSet<>());


                    menuRep.addNewManu(newMenu);
                    Set<Dish> menuDishes = new HashSet<>();
                    for (DishDto dish : menu.getDishes()) {
                        idUtils = new IdUtils();
                        Dish exist = dishRep.getDishByName(dish.getName());

                        if (exist == null) {
                            Dish newDish = new Dish()
                                    .setMenu(newMenu)
                                    .setName(dish.getName())
                                    .setCost(dish.getCost())
                                    .setDescription(dish.getDescription())
                                    .setId(idUtils.generateUuid())
                                    .setReviews(new HashSet<>());
                            dishRep.createDish(newDish);
                            menuDishes.add(newDish);
                        }
                            // add warn logging, the dish is already exists in the chosen menu
                    }
                    newMenu.setDishes(menuDishes);
                    menus.add(newMenu);
                    menuRep.updateMenu(newMenu);
                }
                // logg warn, the menu is already exists;
            }
            restaurant.setMenus(menus);
            return RestaurantMapper.toRestaurantDto(restaurantRep.updateRestaurant(restaurant));
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.DUPLICATE_ENTITY, email);
    }

    @Override
    public RestaurantService updateRestaurantProfile(RestaurantDto restaurantDto) {
        return null;
    }

    @Override
    public List<RestaurantDto> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRep.getAllRestaurants();

        return new ArrayList<>(restaurants
                .stream()
                .map(restaurant -> new ModelMapper().map(restaurant, RestaurantDto.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public List<OrderDto> getAllOrders(String restaurantName) {
        Restaurant restaurant = restaurantRep.getRestaurant(restaurantName);
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
    public String changeOrderStatus(String orderId, String restaurantName, OrderStatusType status) {
        Restaurant restaurant = restaurantRep.getRestaurant(restaurantName);
        IdUtils idUtils = new IdUtils();
        if (restaurant != null) {
            if (idUtils.validateUuid(orderId)) {
                Order order = orderRep.getOrder(orderId);
                if (order != null) {
                    order.setOrderStatusType(status);
                    orderRep.updateOrderProfile(order);
                    return "Order has been updated successfully";
                }
                throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND, orderId);
            }
            throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, orderId);
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND, restaurantName);
    }

    @Override
    public List<Restaurant> getMostPopularRestaurants() {
        return null;
    }

    @Override
    public Restaurant getRestaurantByName(String name) {
        return null;
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
