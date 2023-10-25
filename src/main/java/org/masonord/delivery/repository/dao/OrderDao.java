package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.Order;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.OrderDaoInterface;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao extends AbstractHibernateDao<Order> implements OrderDaoInterface {

    @Override
    public Order createOrder(Order order) {
        return create(order);
    }

    @Override
    public List<Order> getOrders() {
        return getAll();
    }

    @Override
    public Order getOrder(String id) {
        return getById(id);
    }
}
