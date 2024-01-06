package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.service.interfaces.RestaurantService;

public interface RestaurantDaoInterface {

    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurant(String name);
}
