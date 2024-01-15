package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.ReviewDto;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Dish;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.stream.Collectors;

public class MenuMapper {

    public static MenuDto toMenuDto(Menu menu) {
        return new MenuDto()
                .setMenuType(menu.getMenuType())
                .setName(menu.getName())
                .setDescription(menu.getDescription())
                .setReview(new HashSet<>(menu
                        .getReviews()
                        .stream()
                        .map(review -> new ModelMapper().map(review, ReviewDto.class))
                        .collect(Collectors.toList())
                ))
                .setDishes(new HashSet<>(menu
                        .getDishes()
                        .stream()
                        .map(dish -> new ModelMapper().map(dish, Dish.class))
                        .collect(Collectors.toList())
                ));
    }
}
