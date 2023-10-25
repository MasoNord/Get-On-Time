package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.Courier;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.CourierDaoInterface;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourierDao extends AbstractHibernateDao<Courier> implements CourierDaoInterface {
    @Override
    public Courier createCourier(Courier courier) {return create(courier);}

    @Override
    public void deleteCourier(String id) {
        deleteById(id);
    }

    @Override
    public Courier getCourier(String id) {
        return getById(id);
    }

    @Override
    public List<Courier> getCouriers() {
        return getAll();
    }
}
