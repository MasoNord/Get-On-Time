package org.masonord.delivery.repository;

import org.masonord.delivery.model.restarurant.Dish;

import java.util.List;

public interface DishRepository {

    Dish createDish(Dish dish);

    Dish getDishByName(String name);

    Dish updateDishRecord(Dish dish);

    Dish getDishByUUID(String uuid);

    List<Dish> getAllDishes(int offset, int limit);

    void deleteDish(Dish dish);
}
