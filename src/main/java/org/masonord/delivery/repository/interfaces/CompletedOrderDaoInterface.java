package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.CompletedOrder;
import java.util.List;

public interface CompletedOrderDaoInterface {
    CompletedOrder addOrder(CompletedOrder CompletedOrder);

    List<CompletedOrder> getCompletedOrders();

}
