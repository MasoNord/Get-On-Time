package org.masonord.delivery.repository.dao;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.RestaurantDaoInterface;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class RestaurantDao extends AbstractHibernateDao<Restaurant> implements RestaurantDaoInterface {
    public RestaurantDao() {
        setClass(Restaurant.class);
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        return create(restaurant);
    }

    @Override
    public Restaurant getRestaurant(String name) {
        return getByName(name);
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant) {
        return update(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return getAll();
    }
}
