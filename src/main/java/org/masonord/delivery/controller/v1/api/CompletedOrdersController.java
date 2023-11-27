package org.masonord.delivery.controller.v1.api;

import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.classes.CompletedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("completedOrderController")
@RequestMapping("/api/v1/completedOrders")
public class CompletedOrdersController {

    @Autowired
    CompletedOrderService completedOrderService;


    @GetMapping("/{email}")
    public Response getCompletedOrdersForCourier(@PathVariable String email) {
        return Response.ok().setPayload(completedOrderService.getCompletedOrdersByCourierEmail(email));
    }

    @GetMapping()
    public Response getAllCompletedOrders() {
        return Response.ok().setPayload(completedOrderService.getAllCompletedOrders());
    }
}
