package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.DishDto;

import java.util.List;

public interface DishService {

    DishDto addNewDish(DishDto dishDto);

    DishDto getDishByName(String name);

    DishDto updateDishRecord(String name);

    List<DishDto> getAllDishes();

    void deleteDish(String name);
}
