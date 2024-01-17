package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;
public interface RestaurantService {

    RestaurantDto addNewRestaurant(RestaurantDto restaurantDto, String email);

    RestaurantService updateRestaurantProfile(RestaurantDto restaurantDto);

    List<RestaurantDto> getAllRestaurants();

    List<Restaurant> getMostPopularRestaurants();

    Restaurant getRestaurantByName(String name);
}
