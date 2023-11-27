package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Location;

public class LocationMapper {

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto()
                .setCity(location.getCity())
                .setNumber(location.getNumber())
                .setCountry(location.getCountry())
                .setZipCode(location.getZipCode())
                .setStreet(location.getStreet());
    }

}
