package org.masonord.delivery.controller.v1.api;

import ch.qos.logback.core.util.Loader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.response.Response;
import org.masonord.delivery.exception.ExceptionHandlerController;
import org.masonord.delivery.service.interfaces.CourierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courier")
@Tag(name = "Courier Api")
public class CourierController {
    private final CourierService courierService;

    @Autowired
    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a courier by email", description = "Courier must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourierDto.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "404", description = "Courier Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<CourierDto> getCourier(@PathVariable String email){
        return ResponseEntity.ok().body(courierService.findCourierByEmail(email));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all couriers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CourierDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<List<CourierDto>> getCouriers(@RequestParam(defaultValue = "0", required = false) int offset,
                                @RequestParam(defaultValue = "10", required = false) int limit) {
        return ResponseEntity.ok().body(courierService.getAllCouriers(offset, limit));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/take-order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Assign a new order to a courier", description = "Courier and Order must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CourierDto.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "404", description = "Courier not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid order ID supplied"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "416", description = "Order has already been assigned"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<CourierDto> setNewOrder(@PathVariable String orderId, HttpServletRequest request) {
        return ResponseEntity.ok().body(courierService.acceptOrder(orderId, request.getUserPrincipal().getName()));
    }

    @RequestMapping(method = RequestMethod.GET, value ="/active-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all courier's active orders", description = "Courier must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "404", description = "Courier not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<List<OrderDto>> getActiveOrders(HttpServletRequest request) {
        return ResponseEntity.ok().body(courierService.getOrders(request.getUserPrincipal().getName()));
    }
}
