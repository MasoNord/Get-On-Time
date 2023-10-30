package org.masonord.delivery.repository.dao;

import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.LocationDaoInterface;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDao extends AbstractHibernateDao<Location> implements LocationDaoInterface {
    @Override
    public Location addNewPlace(Location location) {
        return create(location);
    }
}
