package org.masonord.delivery.controller.v1.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.DishCreateRequest;
import org.masonord.delivery.controller.v1.request.MenuCreateRequest;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.service.interfaces.MenuService;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/menu")
@Tag(name = "Menu API")
public class MenuController {
    private final IdUtils idUtils;
    private final MenuService menuService;

    @Autowired
    public MenuController(IdUtils idUtils, MenuService menuService) {
        this.idUtils = idUtils;
        this.menuService = menuService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add new menu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuDto.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })
    public ResponseEntity<MenuDto> addNewManu(@RequestBody @Valid MenuCreateRequest createRequest) {
        MenuDto menuDto = new MenuDto()
                .setMenuType("")
                .setName(Optional.of(createRequest.getName()).orElse(""))
                .setDescription(createRequest.getDescription())
                .setDishes(new HashSet<>());

        return ResponseEntity.ok(menuService.addNewManu(menuDto, createRequest.getLat(), createRequest.getLon()));
    }
    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a menu by name", description = "Menu must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })
    public ResponseEntity<MenuDto> getMenuByName(@PathVariable String name) {
        return ResponseEntity.ok(menuService.getManuByName(name));
    }

    @RequestMapping(value = "/add-dish/{name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add dishes to a menu", description = "A menu must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MenuDto.class)
                    )
            }
            ),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<MenuDto> addDishesToMenu(@RequestBody @Valid List<DishCreateRequest> dishes, @PathVariable String name) {
        List<DishDto> dishList = new ArrayList<>(dishes
                .stream()
                .map(dish -> new ModelMapper().map(dish, DishDto.class))
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(menuService.addDishes(dishList, name));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all menus")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MenuDto.class))
                    )
            }
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Failure")
    })

    public ResponseEntity<List<MenuDto>> getAllMenues() {
        return ResponseEntity.ok(menuService.getAllMenus());
    }
}
