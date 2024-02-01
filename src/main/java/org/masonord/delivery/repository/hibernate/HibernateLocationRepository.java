package org.masonord.delivery.repository.hibernate;

import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.LocationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HibernateLocationRepository extends AbstractHibernateRep<Location> implements LocationRepository {
    @Override
    public Location addNewPlace(Location location) {
        return create(location);
    }
}
