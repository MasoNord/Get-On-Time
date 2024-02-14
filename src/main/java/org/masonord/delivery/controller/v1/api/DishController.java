package org.masonord.delivery.controller.v1.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.masonord.delivery.controller.v1.request.CreateDishRequest;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.service.interfaces.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dish")
@Tag(name = "Dish Api")
public class DishController {
    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

//    @RequestMapping
//    public ResponseEntity<DishDto> addDish(CreateDishRequest request) {
//
//    }
}

