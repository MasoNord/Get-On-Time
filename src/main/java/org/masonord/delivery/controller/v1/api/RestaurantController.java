package org.masonord.delivery.controller.v1.api;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.RestaurantCreateRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.service.interfaces.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "restaurant", description = "the restaurant API")
@RestController("RestaurantController")
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping()
    public ResponseEntity<RestaurantDto> addNewRestaurant(
            @RequestBody @Valid RestaurantCreateRequest createRequest,
            HttpServletRequest request) {

        LocationDto locationDto = new LocationDto()
                .setCountry(createRequest.getLocation().getCountry())
                .setCity(createRequest.getLocation().getCity())
                .setNumber(createRequest.getLocation().getNumber())
                .setStreet(createRequest.getLocation().getStreet())
                .setZipCode(createRequest.getLocation().getZipCode());


        RestaurantDto restaurantDto = new RestaurantDto()
                .setLocation(locationDto)
                .setName(createRequest.getName());
        return ResponseEntity.ok().body(restaurantService.addNewRestaurant(restaurantDto, request.getUserPrincipal().getName()));
    }

//    @GetMapping()
//    public ResponseEntity<RestaurantDto> getRestaurants() {
//        return ResponseEntity.ok().body()
//    }

}
