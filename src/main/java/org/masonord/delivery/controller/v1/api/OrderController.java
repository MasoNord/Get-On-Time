package org.masonord.delivery.controller.v1.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.ChangeOrderStatusRequest;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.controller.v1.request.OrderCreateRequest;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.service.classes.OrderServiceImpl;
import org.masonord.delivery.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{restaurantName}")
    public ResponseEntity<OrderDto> addOrder(
            @RequestBody @Valid OrderCreateRequest orderCreateRequest,
            @PathVariable String restaurantName,
            HttpServletRequest request) {
        return ResponseEntity.ok().body(
                orderService.addNewOrder(
                    restaurantName,
                    request.getUserPrincipal().getName(),
                    orderCreateRequest.getDishes()
                )
        );
    }

    @PostMapping("/complete")
    public ResponseEntity<CompletedOrderDto> completeOrder(@RequestBody @Valid OrderCompleteRequest orderCompleteRequest) {
        return ResponseEntity.ok().body(orderService.completeOrder(orderCompleteRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getOrders(
                    @RequestParam(defaultValue = "0", required = false) int offset,
                    @RequestParam(defaultValue = "10", required = false) int limit){
        return ResponseEntity.ok().body(orderService.getOrders(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }

    @PutMapping(value = "/change-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeOrderStatus(@RequestBody @Valid ChangeOrderStatusRequest request) {
        return ResponseEntity.ok().body(orderService.changeOrderStatus(request.getOrderId(), request.getRestaurantName(), request.getStatus()));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteOrderById(@PathVariable String id) {
        return ResponseEntity.ok().body(orderService.deleteOrder(id));
    }
}
