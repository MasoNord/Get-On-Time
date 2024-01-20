package org.masonord.delivery.controller.v1.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.controller.v1.request.OrderCreateRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.classes.OrderServiceImpl;
import org.masonord.delivery.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("OrderController")
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/{restaurantName}")
    public Response addOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest, @PathVariable String restaurantName, HttpServletRequest request) {
        return Response.ok().setPayload(orderService.addNewOrder(restaurantName, request.getUserPrincipal().getName(), orderCreateRequest.getDishes()));
    }

    @PostMapping("/complete")
    public Response completeOrder(@RequestBody @Valid OrderCompleteRequest orderCompleteRequest) {
        return Response.ok().setPayload(orderService.completeOrder(orderCompleteRequest));
    }

    @GetMapping()
    public Response getOrders(@RequestParam(defaultValue = "0", required = false) int offset,
                              @RequestParam(defaultValue = "10", required = false) int limit){
        return Response.ok().setPayload(orderService.getOrders(new OffsetBasedPageRequest(offset, limit)));
    }
    @GetMapping("/{id}")
    public Response getOrderById(@PathVariable String id) {
        return Response.ok().setPayload(orderService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    public Response deleteOrderById(@PathVariable String id) {
        return Response.ok().setPayload(orderService.deleteOrder(id));
    }
}
