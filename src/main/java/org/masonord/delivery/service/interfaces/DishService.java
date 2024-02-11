package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.DishDto;

import java.util.List;

public interface DishService {

    DishDto addNewDish(DishDto dishDto, String menuName, double lat, double lon);

    DishDto getDishByName(String name);

    List<DishDto> getAllDishes(int offset, int limit);
}
