package org.masonord.delivery.repository;

import org.masonord.delivery.model.Location;

public interface LocationRepository {

    Location addNewPlace(Location location);

    Location getLocationByCoordinates(double lat, double lon);

}
