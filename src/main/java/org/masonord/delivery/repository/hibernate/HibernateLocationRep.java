package org.masonord.delivery.repository.hibernate;

import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.LocationRep;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HibernateLocationRep extends AbstractHibernateRep<Location> implements LocationRep {
    @Override
    public Location addNewPlace(Location location) {
        return create(location);
    }
}
