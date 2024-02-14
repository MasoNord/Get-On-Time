package org.masonord.delivery.controller.v1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.service.interfaces.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DefaultEditorKit;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@Tag(name = "Customer API")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a customer", description = "Customer must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<CustomerDto> getCustomer(@PathVariable String email) {
        return  ResponseEntity.ok().body(customerService.findCustomerByEmail(email));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all couriers", description = "Using pagination technique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    @GetMapping()
    public ResponseEntity<List<CustomerDto>> getCustomers(@RequestParam(defaultValue = "0", required = false) int offset, @RequestParam(defaultValue = "10", required = false) int limit){
        return ResponseEntity.ok().body(customerService.getCustomers(offset, limit));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/active-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all customer's active orders", description = "Customer must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Email is null"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    @GetMapping("/active-orders")
    public ResponseEntity<List<OrderDto>> getActiveOrders(HttpServletRequest request) {
        return ResponseEntity.ok().body(customerService.getActiveOrders(request.getUserPrincipal().getName()));
    }
}
