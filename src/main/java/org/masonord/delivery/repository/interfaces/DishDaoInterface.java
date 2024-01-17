package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.restarurant.Dish;

import java.util.List;

public interface DishDaoInterface {

    Dish createDish(Dish dish);

    Dish getDishByName(String name);

    Dish updateDishRecord(Dish dish);

    Dish getDishByUUID(String uuid);

    List<Dish> getAllDishes();

    void deleteDish(Dish dish);
}
