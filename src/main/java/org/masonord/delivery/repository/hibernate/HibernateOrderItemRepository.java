package org.masonord.delivery.repository.hibernate;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class HibernateOrderItemRepository extends AbstractHibernateRep<OrderItem> implements OrderItemRepository {
    public HibernateOrderItemRepository() {
        setClass(OrderItem.class);
    }

    @Override
    public OrderItem addNewOrderItem(OrderItem items) {
        return create(items);
    }

}
