package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Location;

public interface LocationService {

    /**
     * Register a new location based on place (street, country, zip code, number)
     *
     * @param locationDto
     * @return
     *
     */
    Location addNewPlaceByName(LocationDto locationDto);

    /**
     * Calculate distance between two points
     * and return result in meters using Harvesine formula
     *
     *
     * @param lat1
     * @param lon2
     * @param lat1
     * @param lon2
     * @return
     */

     static double getDistanceFromLatLonInM(double lat1, double lat2, double lon1, double lon2) {
        return Math.cos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) *  Math.cos(lon2 - lon1)) * 6371;
     }
}
