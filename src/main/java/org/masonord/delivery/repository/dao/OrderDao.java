package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.OrderDaoInterface;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public class OrderDao extends AbstractHibernateDao<Order> implements OrderDaoInterface {
    public OrderDao() {
        setClass(Order.class);
    }

    @Override
    public Order createOrder(Order order) {
        return create(order);
    }

    @Override
    public List<Order> getOrders() {
        return getAll();
    }

    @Override
    public Order updateOrderProfile(Order order) {
        return update(order);
    }

    @Override
    public List<Order> getOrders(int offset, int limit) {
        return getAll(offset, limit);
    }

    @Override
    public Order getOrder(String id) {
        return getByUUID(id);
    }

    @Override
    public void deleteOrder(Order order) {
        delete(order);
    }
}
