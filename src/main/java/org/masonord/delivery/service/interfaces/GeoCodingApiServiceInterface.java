package org.masonord.delivery.service.interfaces;

public interface GeoCodingApiServiceInterface {
    /**
     * Get location details of a given place
     * @param address
     * @return
     *
     */
    Object[] getGeoLocation(String address);
}
