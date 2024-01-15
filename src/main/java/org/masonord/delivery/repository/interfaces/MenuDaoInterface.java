package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.restarurant.Menu;

import java.util.List;

public interface MenuDaoInterface {

    Menu addNewManu(Menu menu);

    Menu updateMenu(Menu menu);

    Menu getMenuByName(String name);

    List<Menu> getAllMenus();

    void deleteMenu(Menu menu);
}
