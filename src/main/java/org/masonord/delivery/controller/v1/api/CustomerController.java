package org.masonord.delivery.controller.v1.api;


import org.masonord.delivery.controller.v1.request.LocationAddRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.classes.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("CustomerController")
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{email}")
    public Response getCustomer(@PathVariable String email) {
        return Response.ok().setPayload(customerService.findCustomerByEmail(email));
    }
//(params = {"page", "size"})

    @GetMapping()
    public Response getCustomers() {
        return Response.ok().setPayload(customerService.getCustomers());

    }

    @PutMapping("/{email}")
    public Response updateCurrentLocation(@RequestBody LocationAddRequest locationAddRequest, @PathVariable String email) {
        LocationDto locationDto = new LocationDto()
                .setStreet(locationAddRequest.getStreet())
                .setCity(locationAddRequest.getCity())
                .setCountry(locationAddRequest.getCountry())
                .setZipCode(locationAddRequest.getZipCode())
                .setNumber(locationAddRequest.getNumber());

        return Response.ok().setPayload(customerService.updateCurrentLocation(locationDto, email));
    }

}
