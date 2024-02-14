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
import org.masonord.delivery.controller.v1.request.OrderCreateRequest;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@Tag(name = "Order Api")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/{restaurantName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new order", description = "Customer and restaurant must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

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

    @RequestMapping(value = "/complete/{orderId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Complete order", description = "Courier and order must exist, also an order has to be assigned to a courier before performing operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CompletedOrderDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied (not UUID)"),
            @ApiResponse(responseCode = "404", description = "Courier not found"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "416", description = "Order has already been assigned"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<CompletedOrderDto> completeOrder(@PathVariable String orderId, HttpServletRequest request) {
        return ResponseEntity.ok().body(orderService.completeOrder(request.getUserPrincipal().getName(), orderId));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all menus", description = "Using pagination technique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getOrders(
                    @RequestParam(defaultValue = "0", required = false) int offset,
                    @RequestParam(defaultValue = "10", required = false) int limit){
        return ResponseEntity.ok().body(orderService.getOrders(offset, limit));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get order by ID", description = "Order must exist and ID must be valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied (not UUID)"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }

    @RequestMapping(value =  "/change-status", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Change order status", description = "Order must exist and ID must be valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied (not UUID)"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<String> changeOrderStatus(@RequestBody @Valid ChangeOrderStatusRequest request) {
        return ResponseEntity.ok().body(orderService.changeOrderStatus(request.getOrderId(), request.getRestaurantName(), request.getStatus()));
    }

@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remove order by ID", description = "Order must exist and ID must be valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<String> deleteOrderById(@PathVariable String id) {
        return ResponseEntity.ok().body(orderService.deleteOrder(id));
    }
}
