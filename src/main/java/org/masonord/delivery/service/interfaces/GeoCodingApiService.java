package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.GeoCodingDto;

public interface GeoCodingApiService {
    /**
     * Get location details of a given place
     * @param address
     * @return
     *
     */
    GeoCodingDto[] getGeoLocation(String address);
}
