package org.masonord.delivery.controller.v1.api;

import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.interfaces.CompletedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/completedOrders")
public class CompletedOrdersController {

    private final CompletedOrderService completedOrderService;
    @Autowired
    public CompletedOrdersController(CompletedOrderService completedOrderService) {
        this.completedOrderService = completedOrderService;
    }

    @GetMapping("/{email}")
    public ResponseEntity getCompletedOrdersForCourier(@PathVariable String email) {
        return ResponseEntity.ok().body(completedOrderService.getCompletedOrdersByCourierEmail(email));
    }
    @GetMapping()
    public ResponseEntity getAllCompletedOrders() {
        return ResponseEntity.ok().body(completedOrderService.getAllCompletedOrders());
    }
}
