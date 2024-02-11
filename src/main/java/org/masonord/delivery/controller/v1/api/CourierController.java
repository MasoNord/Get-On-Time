package org.masonord.delivery.controller.v1.api;

import jakarta.servlet.http.HttpServletRequest;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.interfaces.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courier")
public class CourierController {

    private final CourierService courierService;
    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<CourierDto> getCourier(@PathVariable String email){
        return ResponseEntity.ok().body(courierService.findCourierByEmail(email));
    }
    @GetMapping()
    public ResponseEntity<List<CourierDto>> getCouriers(@RequestParam(defaultValue = "0", required = false) int offset,
                                @RequestParam(defaultValue = "10", required = false) int limit) {
        return ResponseEntity.ok().body(courierService.getAllCouriers(offset, limit));
    }

    @PutMapping("/take-order/{orderId}")
    public ResponseEntity<CourierDto> setNewOrder(@PathVariable String orderId, HttpServletRequest request) {
        return ResponseEntity.ok().body(courierService.acceptOrder(orderId, request.getUserPrincipal().getName()));
    }

    @GetMapping("/active-orders")
    public ResponseEntity<List<OrderDto>> getActiveOrders(HttpServletRequest request) {
        return ResponseEntity.ok().body(courierService.getOrders(request.getUserPrincipal().getName()));
    }
}
