package org.masonord.delivery.controller.v1.api;

import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.classes.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
