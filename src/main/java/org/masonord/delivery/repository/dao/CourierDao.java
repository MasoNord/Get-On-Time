package org.masonord.delivery.repository.dao;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Courier;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.CourierDaoInterface;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CourierDao extends AbstractHibernateDao<Courier> implements CourierDaoInterface {
    public CourierDao() {
        setClass(Courier.class);
    }

    @Override
    public Courier createCourier(Courier courier) {return create(courier);}

    @Override
    public Courier getCourierById(Long id) {
        return getById(id);
    }

    @Override
    public void deleteCourier(Long id) {
        deleteById(id);
    }

    @Override
    public Courier getCourierByEmail(String email) {
        return getByEmail(email);
    }

    @Override
    public List<Courier> getCouriers() {
        return getAll();
    }
    @Override
    public Courier updateProfile(Courier courier) {
        return update(courier);
    }
}
