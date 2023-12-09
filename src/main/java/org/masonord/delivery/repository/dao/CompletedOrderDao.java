package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.CompletedOrderDaoInterface;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class CompletedOrderDao extends AbstractHibernateDao<CompletedOrder> implements CompletedOrderDaoInterface{
    public CompletedOrderDao() {
        setClass(CompletedOrder.class);
    }
    @Override
    public CompletedOrder addOrder(CompletedOrder CompletedOrder) {
        return create(CompletedOrder);
    }
    @Override
    public List<CompletedOrder> getCompletedOrders() {
        return getAll();
    }
}
