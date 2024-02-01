package org.masonord.delivery.repository;

import org.masonord.delivery.model.order.OrderItem;

public interface OrderItemRepository {

    OrderItem addNewOrderItem(OrderItem items);
}
