package org.masonord.delivery.repository;

import org.masonord.delivery.model.CompletedOrder;
import java.util.List;

public interface CompletedOrderRepository {
    CompletedOrder addOrder(CompletedOrder CompletedOrder);

    List<CompletedOrder> getCompletedOrders();

}
