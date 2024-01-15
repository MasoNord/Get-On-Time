package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.MenuMapper;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.repository.dao.DishDao;
import org.masonord.delivery.repository.dao.MenuDao;
import org.masonord.delivery.service.interfaces.MenuService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service("MenuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private IdUtils idUtils;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private DishDao dishDao;


    @Override
    public MenuDto addNewManu(MenuDto menuDto) {
        Menu menu = menuDao.getMenuByName(menuDto.getName());

        if (menu == null) {
            menu = new Menu()
                    .setId(idUtils.generateUuid())
                    .setMenuType(menuDto.getMenuType())
                    .setName(menuDto.getName())
                    .setDescription(menuDto.getDescription())
                    .setDishes(menuDto.getDishes())
                    .setDu(DateUtils.todayToStr())
                    .setDc(DateUtils.todayToStr())
                    .setReviews(new HashSet<>());

            return MenuMapper.toMenuDto(menuDao.addNewManu(menu));
        }

        throw exception(ModelType.MENU, ExceptionType.DUPLICATE_ENTITY);
    }

    @Override
    public MenuDto getManuByName(String name) {
        Menu menu = menuDao.getMenuByName(name);

        if (menu != null) {
            return MenuMapper.toMenuDto(menu);
        }

        throw exception(ModelType.MENU, ExceptionType.ENTITY_NOT_FOUND, name);
    }

    @Override
    public MenuDto updateMenu(MenuDto menu) {
        return null;
    }

    @Override
    public List<MenuDto> getAllMenus() {
        return null;
    }

    @Override
    public void removeMenu(String name) {

    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
