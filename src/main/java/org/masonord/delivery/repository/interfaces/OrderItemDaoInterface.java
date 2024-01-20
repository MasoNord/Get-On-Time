package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.OrderItemDto;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.repository.dao.OrderItemDao;

public interface OrderItemDaoInterface {

    OrderItem addNewOrderItem(OrderItem items);
}
