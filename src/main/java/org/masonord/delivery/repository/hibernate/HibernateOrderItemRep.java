package org.masonord.delivery.repository.hibernate;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.repository.OrderItemRep;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class HibernateOrderItemRep extends AbstractHibernateRep<OrderItem> implements OrderItemRep {
    public HibernateOrderItemRep() {
        setClass(OrderItem.class);
    }

    @Override
    public OrderItem addNewOrderItem(OrderItem items) {
        return create(items);
    }

}
