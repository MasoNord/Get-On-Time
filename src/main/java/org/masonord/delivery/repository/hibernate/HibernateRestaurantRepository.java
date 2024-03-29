package org.masonord.delivery.repository.hibernate;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.RestaurantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class HibernateRestaurantRepository extends AbstractHibernateRep<Restaurant> implements RestaurantRepository {
    public HibernateRestaurantRepository() {
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
    public Restaurant findByCoordinates(double lat, double lon) {
        return getByCoordinates(lat, lon);
    }

    @Override
    public List<Restaurant> getAllRestaurants(int offset, int limit) {
        return getAll(offset, limit);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return getAll();
    }
}
