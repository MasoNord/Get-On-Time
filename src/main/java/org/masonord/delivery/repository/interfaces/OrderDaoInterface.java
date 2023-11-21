package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Order;

import java.util.List;

public interface OrderDaoInterface {
    Order createOrder(Order order);

    List<Order> getOrders();

    Order updateOrderProfile(Order order);

    List<Order> getOrders(int offset, int limit);

    Order getOrder(String id);

    void deleteOrder(Order order);

}
