package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.MenuDto;

import java.util.List;

public interface MenuService {

    MenuDto addNewManu(MenuDto menu);

    MenuDto getManuByName(String name);

    MenuDto updateMenu(MenuDto menu);

    List<MenuDto> getAllMenus();

    void removeMenu(String name);

}
