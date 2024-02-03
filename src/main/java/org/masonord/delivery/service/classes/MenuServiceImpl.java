package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.MenuMapper;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.repository.DishRepository;
import org.masonord.delivery.repository.MenuRepository;
import org.masonord.delivery.service.interfaces.MenuService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("MenuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private ExceptionHandler exceptionHandler;


    @Override
    public MenuDto addNewManu(MenuDto menuDto) {
        Menu menu = menuRepository.getMenuByName(menuDto.getName());
        IdUtils idUtils = new IdUtils();
        if (menu == null) {
            menu = new Menu()
                    .setId(idUtils.generateUuid())
                    .setMenuType(menuDto.getMenuType())
                    .setName(menuDto.getName())
                    .setDescription(menuDto.getDescription())
                    .setDu(DateUtils.todayToStr())
                    .setDc(DateUtils.todayToStr())
                    .setDishes(new HashSet<>())
                    .setReviews(new HashSet<>());

            return MenuMapper.toMenuDto(menuRepository.addNewManu(menu));
        }

        throw exception(ModelType.MENU, ExceptionType.DUPLICATE_ENTITY);
    }

    @Override
    public MenuDto addDishes(List<DishDto> dishes, String menuName) {
        Menu menu = menuRepository.getMenuByName(menuName);

        if (menu != null) {
            Set<Dish> menuDishes = menu.getDishes();
            for (DishDto dish : dishes) {
                Dish exist = dishRepository.getDishByName(dish.getName());

                if (exist == null) {
                    IdUtils idUtils = new IdUtils();
                    Dish newDish = new Dish()
                            .setMenu(menu)
                            .setName(dish.getName())
                            .setCost(dish.getCost())
                            .setDescription(dish.getDescription())
                            .setId(idUtils.generateUuid())
                            .setReviews(new HashSet<>());
                    dishRepository.createDish(newDish);
                    menuDishes.add(newDish);
                }
                // add warn logging, the dish is already exists in the chosen menu
            }

            menu.setDishes(menuDishes);
            return MenuMapper.toMenuDto(menuRepository.updateMenu(menu));
        }
        throw exception(ModelType.MENU, ExceptionType.ENTITY_NOT_FOUND, menuName);
    }

    @Override
    public MenuDto getManuByName(String name) {
        Menu menu = menuRepository.getMenuByName(name);

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
        List<Menu> menus = menuRepository.getAllMenus();
        return new ArrayList<>(menus
                .stream()
                .map(menu -> new ModelMapper().map(menu, MenuDto.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public void removeMenu(String name) {

    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
