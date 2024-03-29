package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.dto.model.ReviewDto;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.stream.Collectors;

public class RestaurantMapper {

    public static RestaurantDto toRestaurantDto(Restaurant restaurant) {
        return new RestaurantDto()
                .setLocation(LocationMapper.toLocationDto(restaurant.getLocation()))
                .setName(restaurant.getName())
                .setMenus(new HashSet<>(restaurant
                        .getMenus()
                        .stream()
                        .map(menu -> new ModelMapper().map(menu, MenuDto.class))
                        .collect(Collectors.toList())
                ))
                .setReviews(new HashSet<>(restaurant
                        .getReviews()
                        .stream()
                        .map(review -> new ModelMapper().map(review, ReviewDto.class))
                        .collect(Collectors.toList())
                ))
                .setOrders(new HashSet<>(restaurant
                        .getOrders()
                        .stream()
                        .map(order -> new ModelMapper().map(order, OrderDto.class))
                        .collect(Collectors.toList()))
                );
    }
}
