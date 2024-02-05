package org.masonord.delivery.repository.hibernate;

import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.LocationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HibernateLocationRepository extends AbstractHibernateRep<Location> implements LocationRepository {

    public HibernateLocationRepository() {
        setClass(Location.class);
    }

    @Override
    public Location addNewPlace(Location location) {
        return create(location);
    }

    @Override
    public Location getLocationByCoordinates(double lat, double lon) {
        return getByCoordinates(lat, lon);
    }
}
