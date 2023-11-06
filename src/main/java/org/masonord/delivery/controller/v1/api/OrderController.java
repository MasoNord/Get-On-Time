package org.masonord.delivery.controller.v1.api;


import jakarta.validation.Valid;
import org.hibernate.validator.constraints.UUID;
import org.masonord.delivery.controller.v1.request.OrderCreateRequest;
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
        Location location = new Location()
                .setCountry(CountryType.valueOf(orderCreateRequest.getLocation().getCountry().toString()))
                .setStreet(orderCreateRequest.getLocation().getStreet())
                .setNumber(orderCreateRequest.getLocation().getNumber())
                .setZipCode(orderCreateRequest.getLocation().getZipCode())
                .setCoordinates(new float[] {1.3f, 3.2f});

        OrderDto orderDto = new OrderDto()
                .setLocation(location)
                .setCustomer(customerDao.getCustomerByEmail(orderCreateRequest.getCustomerEmail()))
                .setCost(orderCreateRequest.getCost())
                .setWeight(orderCreateRequest.getWeight())
                .setDeliveryHours(orderCreateRequest.getDeliveryHours());

        return Response.ok().setPayload(orderService.addNewOrder(orderDto));
    }

    @GetMapping()
    public Response getOrders() {
        return Response.ok().setPayload(orderService.getOrders());
    }
    @GetMapping("/{id}")
    public Response getOrderById(@PathVariable  String id) {
        return Response.ok().setPayload(orderService.getOrderById(id));
    }
}
