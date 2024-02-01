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
     * Register a new location based on latitude and longitude
     *
     * @param latitude
     * @param longitude
     * @return
     */

    Location addNewPlaceByCoordinates(float latitude, float longitude);

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
         lon1 = Math.toRadians(lon1);
         lon2 = Math.toRadians(lon2);
         lat1 = Math.toRadians(lat1);
         lat2 = Math.toRadians(lat2);

         double dlon = lon2 - lon1;
         double dlat = lat2 - lat1;
         double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

         double c = 2 * Math.asin(Math.sqrt(a));

         double r = 6371;

         return(c * r * 1000);
     }
}
