package org.masonord.delivery.controller.v1.api;


import jakarta.validation.Valid;
import org.masonord.delivery.controller.v1.request.DishCreateRequest;
import org.masonord.delivery.controller.v1.request.MenuCreateRequest;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.service.interfaces.MenuService;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController("MenuController")
@RequestMapping("/api/v1/menu")
public class MenuController {

    @Autowired
    private IdUtils idUtils;

    @Autowired
    private MenuService menuService;

    @PostMapping()
    public ResponseEntity<MenuDto> addNewManu(@RequestBody @Valid MenuCreateRequest createRequest) {
        MenuDto menuDto = new MenuDto()
                .setMenuType("")
                .setName(Optional.of(createRequest.getName()).orElse(""))
                .setDescription(createRequest.getDescription())
                .setDishes(new HashSet<>());

        return ResponseEntity.ok(menuService.addNewManu(menuDto));
    }

    @GetMapping("/{name}")
    public ResponseEntity<MenuDto> getMenuByName(@PathVariable String name) {
        return ResponseEntity.ok(menuService.getManuByName(name));
    }

//    @DeleteMapping("/{name}")
//    public ResponseEntity<Void> deleteMenuByName(@PathVariable String name) {
//        return ResponseEntity.ok(menuService.removeMenu(name));
//    }
}
