package org.masonord.delivery.service.classes;

import org.masonord.delivery.config.PropertiesConfig;
import org.masonord.delivery.dto.model.GeoCodingDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.LocationRepository;
import org.masonord.delivery.service.interfaces.GeoCodingApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("LocationService")
public class LocationServiceImpl implements org.masonord.delivery.service.interfaces.LocationService {
    private final LocationRepository locationRepository;
    private final GeoCodingApiService geoCodingApiService;
    private final Environment env;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    LocationServiceImpl(LocationRepository locationRepository,
                        GeoCodingApiService geoCodingApiService,
                        Environment env,
                        ExceptionHandler exceptionHandler) {
        this.locationRepository = locationRepository;
        this.geoCodingApiService = geoCodingApiService;
        this.exceptionHandler = exceptionHandler;
        this.env = env;
    }

    @Override
    public Location addNewPlaceByName(LocationDto locationDto) {
        String address = locationDto.getNumber() + "+" + locationDto.getStreet() + "+" + locationDto.getCity() + "+" + locationDto.getZip()
                + "+" + locationDto.getCountry() + "&api_key=" + env.getProperty("geocoding");
        GeoCodingDto[] coordinates = geoCodingApiService.getGeoLocation(address);

        if (coordinates.length != 0) {
            Location location = locationRepository.getLocationByCoordinates(coordinates[0].getLat(), coordinates[0].getLon());
            if (Objects.isNull(location)) {
                location = new Location()
                        .setLatitude(coordinates[0].getLat())
                        .setLongitude(coordinates[0].getLon())
                        .setStreet(locationDto.getStreet())
                        .setCountry(locationDto.getCountry())
                        .setZipCode(locationDto.getZip())
                        .setNumber(locationDto.getNumber())
                        .setCity(locationDto.getCity());
                return locationRepository.addNewPlace(location);
            }
            return location;
        }
        throw exception(ModelType.LOCATION, ExceptionType.ENTITY_NOT_FOUND, address);
    }

    @Override
    public Location addNewPlaceByCoordinates(float latitude, float longitude) {
        return null;
    }

    private  RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
