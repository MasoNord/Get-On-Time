package org.masonord.delivery.repository.dao;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.repository.AbstractHibernateDao;
import org.masonord.delivery.repository.interfaces.MenuDaoInterface;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class MenuDao extends AbstractHibernateDao<Menu> implements MenuDaoInterface {

    public MenuDao() {setClass(Menu.class);}

    @Override
    public Menu addNewManu(Menu menu) {
        return create(menu);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        return update(menu);
    }

    @Override
    public Menu getMenuByName(String name) {
        return getByName(name);
    }

    @Override
    public List<Menu> getAllMenus() {
        return getAll();
    }

    @Override
    public void deleteMenu(Menu menu) {
        delete(menu);
    }
}
