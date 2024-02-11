package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.DishCreateRequest;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;

import java.util.List;
import java.util.Set;

public interface MenuService {

    MenuDto addNewManu(MenuDto menuDto, double lat, double lon);

    MenuDto getManuByName(String name);

    List<MenuDto> getAllMenus();

    MenuDto addDishes(List<DishDto> dishes, String menuName);
}
