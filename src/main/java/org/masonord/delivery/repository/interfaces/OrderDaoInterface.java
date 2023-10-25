package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Order;

import java.util.List;

public interface OrderRepository {
    Order addOrder(OrderDto orderDto);

    List<Order> getAll();

    Order getById(String id);

}
