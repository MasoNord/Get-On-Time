package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.Order;

public interface CompletedOrderRepository {
    void addOrder(Order order);
}
