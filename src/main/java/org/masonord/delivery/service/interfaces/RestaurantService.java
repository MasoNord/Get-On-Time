package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.SetReviewRequest;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.enums.OrderStatusType;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;
public interface RestaurantService {

    RestaurantDto addNewRestaurant(RestaurantDto restaurantDto);

    List<RestaurantDto> getClosestRestaurants(String courierEmail);

    List<RestaurantDto> getAllRestaurants(int offset, int limit);

    List<OrderDto> getAllOrders(String restaurantName);

    List<Restaurant> getMostPopularRestaurants();

    Restaurant getRestaurantByName(String name);

    RestaurantDto setReview(SetReviewRequest request);
}
