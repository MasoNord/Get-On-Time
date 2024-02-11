package org.masonord.delivery.controller.v1.api;

import jakarta.servlet.http.HttpServletRequest;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable String email) {
        return ResponseEntity.ok().body(customerService.findCustomerByEmail(email));
    }
    @GetMapping()
    public ResponseEntity<List<CustomerDto>> getCustomers(@RequestParam(defaultValue = "0", required = false) int offset, @RequestParam(defaultValue = "10", required = false) int limit){
        return ResponseEntity.ok().body(customerService.getCustomers(offset, limit));
    }

    @GetMapping("/active-orders")
    public ResponseEntity<List<OrderDto>> getActiveOrders(HttpServletRequest request) {
        return ResponseEntity.ok().body(customerService.getActiveOrders(request.getUserPrincipal().getName()));
    }
}
