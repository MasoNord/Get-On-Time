package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.ReviewDto;
import org.masonord.delivery.model.restarurant.Dish;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.stream.Collectors;

public class DishMapper {
    public static DishDto toDishDto(Dish dish) {
        return new DishDto()
                .setCost(dish.getCost())
                .setName(dish.getName())
                .setOrderItem(dish.getOrderItem())
                .setDescription(dish.getDescription())
                .setReviews(new HashSet<>(dish
                        .getReviews()
                        .stream()
                        .map(review -> new ModelMapper().map(review, ReviewDto.class)).collect(Collectors.toList()))
                );

    }
}
