package org.masonord.delivery.repository.hibernate;

import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.repository.CompletedOrderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class HibernateCompleteOrderRepository extends AbstractHibernateRep<CompletedOrder> implements CompletedOrderRepository {
    public HibernateCompleteOrderRepository() {
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
