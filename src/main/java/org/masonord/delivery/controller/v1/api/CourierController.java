package org.masonord.delivery.controller.v1.api;

import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.LocationAddRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.classes.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("CourierController")
@RequestMapping("/api/v1/courier")
public class CourierController {
    @Autowired
    private CourierService courierService;

    @GetMapping("/{email}")
    public Response getCourier(@PathVariable String email){
        return Response.ok().setPayload(courierService.findCourierByEmail(email));
    }

    @GetMapping()
    public Response getCouriers() {
        return Response.ok().setPayload(courierService.getAllCouriers());
    }

    @PutMapping("/{email}")
    public Response setCurrentLocation(@RequestBody @Valid LocationAddRequest locationAddRequest, @PathVariable String email) {
        LocationDto locationDto = new LocationDto()
                .setZipCode(locationAddRequest.getZipCode())
                .setNumber((locationAddRequest.getNumber()))
                .setCity(locationAddRequest.getCity())
                .setCountry(locationAddRequest.getCountry())
                .setStreet(locationAddRequest.getStreet());

        return Response.ok().setPayload(courierService.updateCurrentLocation(locationDto, email));
    }
}
