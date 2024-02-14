package org.masonord.delivery.controller.v1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.service.interfaces.CompletedOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/completedOrders")
@Tag(name = "Completed Order Api")
public class CompletedOrdersController {
    private final CompletedOrderService completedOrderService;

    @Autowired
    public CompletedOrdersController(CompletedOrderService completedOrderService) {
        this.completedOrderService = completedOrderService;
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all courier's completed orders", description = "A courier must exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompletedOrderDto.class))
                    )
                }
            ),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })
    public ResponseEntity<List<CompletedOrderDto>> getCompletedOrdersForCourier(@PathVariable String email) {
        return ResponseEntity.ok().body(completedOrderService.getCompletedOrdersByCourierEmail(email));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all completed orders", description = "From every courier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompletedOrderDto.class))
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })
    @GetMapping()
    public ResponseEntity<List<CompletedOrderDto>> getAllCompletedOrders() {
        return ResponseEntity.ok().body(completedOrderService.getAllCompletedOrders());
    }
}
