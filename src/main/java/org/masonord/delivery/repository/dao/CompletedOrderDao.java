package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.CompletedOrderDaoInterface;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class CompletedOrderDao extends AbstractHibernateDao<CompletedOrder> implements CompletedOrderDaoInterface{

    @Override
    public CompletedOrder addOrder(CompletedOrder CompletedOrder) {
        return create(CompletedOrder);
    }
}
