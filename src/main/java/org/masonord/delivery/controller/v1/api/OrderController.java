package org.masonord.delivery.controller.v1.api;

import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.OrderCreateRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.enums.CountryType;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.repository.dao.CustomerDao;
import org.masonord.delivery.service.classes.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("OrderController")
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    CustomerDao customerDao;

    @PostMapping()
    public Response addOrder(@RequestBody @Valid OrderCreateRequest orderCreateRequest) {
        LocationDto locationDto = new LocationDto()
                .setCountry(orderCreateRequest.getLocation().getCountry())
                .setCity(orderCreateRequest.getLocation().getCity())
                .setNumber(orderCreateRequest.getLocation().getNumber())
                .setStreet(orderCreateRequest.getLocation().getStreet())
                .setZipCode(orderCreateRequest.getLocation().getZipCode());

        OrderDto orderDto = new OrderDto()
                .setCustomerEmail(orderCreateRequest.getCustomerEmail())
                .setCost(orderCreateRequest.getCost())
                .setWeight(orderCreateRequest.getWeight())
                .setDeliveryHours(orderCreateRequest.getDeliveryHours());

        return Response.ok().setPayload(orderService.addNewOrder(orderDto, locationDto));
    }

    @GetMapping()
    public Response getOrders() {
        return Response.ok().setPayload(orderService.getOrders());
    }
    @GetMapping("/{id}")
    public Response getOrderById(@PathVariable String id) {
        return Response.ok().setPayload(orderService.getOrderById(id));
    }
}
