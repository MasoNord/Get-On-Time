package org.masonord.delivery.repository;

import org.masonord.delivery.model.restarurant.Menu;

import java.util.List;

public interface MenuRep {

    Menu addNewManu(Menu menu);

    Menu updateMenu(Menu menu);

    Menu getMenuByName(String name);

    List<Menu> getAllMenus();

    void deleteMenu(Menu menu);
}
