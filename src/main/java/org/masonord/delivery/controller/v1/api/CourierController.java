package org.masonord.delivery.controller.v1.api;

import jakarta.servlet.http.HttpServletRequest;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.service.interfaces.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

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
    public Response getCouriers(@RequestParam(defaultValue = "0", required = false) int offset,
                                @RequestParam(defaultValue = "10", required = false) int limit) {
        return Response.ok().setPayload(courierService.getAllCouriers(new OffsetBasedPageRequest(offset, limit)));
    }

    @PutMapping("/order/{orderId}")
    public ResponseEntity acceptOrder(@PathVariable String orderId, HttpServletRequest request) {
        return ResponseEntity.ok().body(courierService.acceptOrder(request.getUserPrincipal().getName(), orderId));
    }

    @PutMapping("/take-order/{orderId}")
    public Response setNewOrder(@PathVariable String orderId, HttpServletRequest request) {
        return Response.ok().setPayload(courierService.setNewOrder(orderId, request.getUserPrincipal().getName()));
    }
    @GetMapping("/meta-info/{email}")
    public Response getMetaInfo(@RequestParam() String startDate, @RequestParam() String endDate, @PathVariable String email) throws ParseException {
        return Response.ok().setPayload(courierService.getMetaInfo(email, startDate, endDate));
    }

    @GetMapping("/active-orders")
    public ResponseEntity<List<OrderDto>> getActiveOrders(HttpServletRequest request) {
        return ResponseEntity.ok().body(courierService.getOrders(request.getUserPrincipal().getName()));
    }
}
