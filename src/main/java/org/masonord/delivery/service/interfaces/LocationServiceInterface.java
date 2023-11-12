package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Location;

public interface LocationServiceInterface {

    /**
     * Register a new location based on place (street, country, zip code, number)
     *
     * @param locationDto
     * @return
     *
     */
    Location addNewPlaceByName(LocationDto locationDto);

    /**
     * Register a new location based on latitude and longitude
     *
     * @param latitude
     * @param longitude
     * @return
     */

    Location addNewPlaceByCoordinates(float latitude, float longitude);
}
