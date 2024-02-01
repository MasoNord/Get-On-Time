package org.masonord.delivery.service.classes;

import org.masonord.delivery.config.PropertiesConfig;
import org.masonord.delivery.dto.model.GeoCodingDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LocationService")
public class LocationServiceImpl implements org.masonord.delivery.service.interfaces.LocationService {
    private static PropertiesConfig environment;

    @Autowired
    LocationServiceImpl(PropertiesConfig propertiesConfig) {
        LocationServiceImpl.environment = propertiesConfig;
    }

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    GeoCodingApiServiceImpl geoCodingApiService;

    @Override
    public Location addNewPlaceByName(LocationDto locationDto) {
        String address = locationDto.getNumber() + "+" + locationDto.getStreet() + "+" + locationDto.getCity() + "+" + locationDto.getZipCode()
                + "+" + locationDto.getCountry() + "&api_key=" + environment.getConfigValue("geocoding");

        GeoCodingDto[] coordinates = geoCodingApiService.getGeoLocation(address);

        if (coordinates.length != 0) {
            Location location = new Location()
                    .setLatitude(coordinates[0].getLat())
                    .setLongitude(coordinates[0].getLon())
                    .setStreet(locationDto.getStreet())
                    .setCountry(locationDto.getCountry())
                    .setZipCode(locationDto.getZipCode())
                    .setNumber(locationDto.getNumber())
                    .setCity(locationDto.getCity());
            return locationRepository.addNewPlace(location);
        }

        throw exception(ModelType.LOCATION, ExceptionType.ENTITY_NOT_FOUND, address);
    }

    @Override
    public Location addNewPlaceByCoordinates(float latitude, float longitude) {
        return null;
    }

    private  RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
