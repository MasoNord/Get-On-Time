package org.masonord.delivery.repository;

import org.masonord.delivery.model.order.OrderItem;

public interface OrderItemRep {

    OrderItem addNewOrderItem(OrderItem items);
}
