package org.masonord.delivery.repository;

import org.masonord.delivery.model.order.Order;
import java.util.List;

public interface OrderRepository {
    Order createOrder(Order order);

    List<Order> getOrders();

    Order updateOrderProfile(Order order);

    List<Order> getOrders(int offset, int limit);

    Order getOrder(String id);

    void deleteOrder(Order order);

}
