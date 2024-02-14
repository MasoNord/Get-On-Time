package org.masonord.delivery.controller.v1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/api/v1/restaurant")
@Tag(name = "Restaurant API")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final OrderService orderService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, OrderService orderService) {
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new Restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantDto.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400", description = "Restaurant with this name has already been added"),
            @ApiResponse(responseCode = "404", description = "Location not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

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

    @RequestMapping(value = "/orders/{restaurantName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all restaurant active orders", description =  "Restaurant must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<List<OrderDto>> getAllOrders(@PathVariable String restaurantName){
        return ResponseEntity.ok().body(restaurantService.getAllOrders(restaurantName));
    }

    @RequestMapping(value = "/orders/change-order-status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change order status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json")
                }
            ),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied (not UUID)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<String> changeOrderStatus(@RequestParam String orderId,
                                              @RequestParam String restaurantName,
                                              @RequestBody @Valid ChangeOrderStatusRequest status) {
        return ResponseEntity.ok().body(orderService.changeOrderStatus(orderId, restaurantName, status.getStatus()));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantDto.class))
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<List<RestaurantDto>> getRestaurants(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "1") int limit) {
        return ResponseEntity.ok().body(restaurantService.getAllRestaurants(offset, limit));
    }

    @RequestMapping(value = "/closest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all restaurants close to the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantDto.class))
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "User has not set a location yet"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })
    public ResponseEntity<List<RestaurantDto>> getClosestRestaurants(HttpServletRequest request) {
        return ResponseEntity.ok().body(restaurantService.getClosestRestaurants(request.getUserPrincipal().getName()));
    }
}
