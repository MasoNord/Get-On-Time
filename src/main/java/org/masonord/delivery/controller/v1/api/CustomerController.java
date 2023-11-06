package org.masonord.delivery.controller.v1.api;


import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.classes.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("CustomerController")
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{email}")
    public Response getCustomer(@PathVariable String email) {
        return Response.ok().setPayload(customerService.findCustomerByEmail(email));
    }

    @GetMapping()
    public Response getCustomers() {
        return Response.ok().setPayload(customerService.getCustomers());

    }
}
