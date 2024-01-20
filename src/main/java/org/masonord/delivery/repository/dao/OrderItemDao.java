package org.masonord.delivery.repository.dao;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.OrderItemDaoInterface;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class OrderItemDao extends AbstractHibernateDao<OrderItem> implements OrderItemDaoInterface {
    public OrderItemDao() {
        setClass(OrderItem.class);
    }

    @Override
    public OrderItem addNewOrderItem(OrderItem items) {
        return create(items);
    }

}
