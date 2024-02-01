package org.masonord.delivery.repository;

import org.masonord.delivery.model.restarurant.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurant(String name);

    Restaurant updateRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();
}
