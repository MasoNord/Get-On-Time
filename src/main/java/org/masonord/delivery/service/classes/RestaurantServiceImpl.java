package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.RestaurantMapper;
import org.masonord.delivery.dto.mapper.UserMapper;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.dao.RestaurantDao;
import org.masonord.delivery.repository.dao.UserDao;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.service.interfaces.RestaurantService;
import org.masonord.delivery.service.interfaces.UserService;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service("RestaurantService")
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    LocationService locationService;

    @Autowired
    UserDao userDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    IdUtils idUtils;


    @Override
    public RestaurantDto addNewRestaurant(RestaurantDto restaurantDto, String email) {
        Location location = locationService.addNewPlaceByName(restaurantDto.getLocation());

        if (restaurantDao.getRestaurant(restaurantDto.getName()) == null) {
            Restaurant restaurant = new Restaurant()
                    .setLocation(location)
                    .setName(restaurantDto.getName())
                    .setUser(userDao.findUserByEmail(email))
                    .setOrders(new HashSet<>())
                    .setMenues(new HashSet<>())
                    .setReviews(new HashSet<>());

            return RestaurantMapper.toRestaurantDto(restaurantDao.createRestaurant(restaurant));
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.DUPLICATE_ENTITY, email);
    }

    @Override
    public RestaurantService updateRestaurantProfile(RestaurantDto restaurantDto) {
        return null;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return null;
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
