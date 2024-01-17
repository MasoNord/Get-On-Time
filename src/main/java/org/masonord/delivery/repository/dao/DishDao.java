package org.masonord.delivery.repository.dao;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.DishDaoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class DishDao extends AbstractHibernateDao<Dish> implements DishDaoInterface {

    @Autowired
    public DishDao() {setClass(Dish.class);}

    @Override
    public Dish createDish(Dish dish) {
        return create(dish);
    }

    @Override
    public Dish getDishByName(String name) {
        return getByName(name);
    }

    @Override
    public Dish getDishByUUID(String uuid) {
        return getByUUID(uuid);
    }

    @Override
    public Dish updateDishRecord(Dish dish) {
        return update(dish);
    }

    @Override
    public List<Dish> getAllDishes() {
        return getAll();
    }

    @Override
    public void deleteDish(Dish dish) {
        delete(dish);
    }
}
