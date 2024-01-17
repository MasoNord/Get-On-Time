package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.service.interfaces.RestaurantService;

import java.util.List;

public interface RestaurantDaoInterface {

    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurant(String name);

    Restaurant updateRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();
}
