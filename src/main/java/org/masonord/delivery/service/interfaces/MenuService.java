package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.DishCreateRequest;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;

import java.util.List;
import java.util.Set;

public interface MenuService {

    MenuDto addNewManu(MenuDto menu);

    MenuDto getManuByName(String name);

    MenuDto updateMenu(MenuDto menu);

    MenuDto addDishes(List<DishDto> dishes, String menuName);

    List<MenuDto> getAllMenus();

    void removeMenu(String name);

}
