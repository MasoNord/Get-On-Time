package org.masonord.delivery.controller.v1.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.ChangeOrderStatusRequest;
import org.masonord.delivery.controller.v1.request.RestaurantCreateRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.RestaurantDto;
import org.masonord.delivery.service.interfaces.OrderService;
import org.masonord.delivery.service.interfaces.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "restaurant", description = "the restaurant API")
@RestController
@RequestMapping("/api/v1/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final OrderService orderService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, OrderService orderService) {
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<RestaurantDto> addNewRestaurant(@RequestBody @Valid RestaurantCreateRequest createRequest) {

        LocationDto locationDto = new LocationDto()
                .setCountry(createRequest.getLocation().getCountry())
                .setCity(createRequest.getLocation().getCity())
                .setNumber(createRequest.getLocation().getNumber())
                .setStreet(createRequest.getLocation().getStreet())
                .setZip(createRequest.getLocation().getZip());

        Set<MenuDto> menus = new HashSet<>(createRequest.getMenus()
                .stream()
                .map(menuCreateRequest -> new ModelMapper().map(menuCreateRequest, MenuDto.class))
                .collect(Collectors.toList())
        );


        RestaurantDto restaurantDto = new RestaurantDto()
                .setLocation(locationDto)
                .setName(createRequest.getName())
                .setMenus(menus);
        return ResponseEntity.ok().body(restaurantService.addNewRestaurant(restaurantDto));
    }

    @GetMapping("/orders/{restaurantName}")
    public ResponseEntity<List<OrderDto>> getAllOrders(@PathVariable String restaurantName){
        return ResponseEntity.ok().body(restaurantService.getAllOrders(restaurantName));
    }

    @PutMapping(value = "/orders/change-order-status",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changeOrderStatus(@RequestParam String orderId,
                                              @RequestParam String restaurantName,
                                              @RequestBody @Valid ChangeOrderStatusRequest status) {
        return ResponseEntity.ok().body(orderService.changeOrderStatus(orderId, restaurantName, status.getStatus()));
    }

    @GetMapping()
    public ResponseEntity<List<RestaurantDto>> getRestaurants(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "1") int limit) {
        return ResponseEntity.ok().body(restaurantService.getAllRestaurants(offset, limit));
    }

    @GetMapping("/closest")
    public ResponseEntity<List<RestaurantDto>> getClosestRestaurants(HttpServletRequest request) {
        return ResponseEntity.ok().body(restaurantService.getClosestRestaurants(request.getUserPrincipal().getName()));
    }
}
