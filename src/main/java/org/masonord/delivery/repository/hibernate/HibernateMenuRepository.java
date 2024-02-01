package org.masonord.delivery.repository.hibernate;

import jakarta.transaction.Transactional;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.repository.MenuRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Transactional
public class HibernateMenuRepository extends AbstractHibernateRep<Menu> implements MenuRepository {

    public HibernateMenuRepository() {setClass(Menu.class);}

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
