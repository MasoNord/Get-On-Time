package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.MenuMapper;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.DishRepository;
import org.masonord.delivery.repository.MenuRepository;
import org.masonord.delivery.repository.RestaurantRepository;
import org.masonord.delivery.service.interfaces.MenuService;
import org.masonord.delivery.service.interfaces.UserService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final ExceptionHandler exceptionHandler;
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository,
                           DishRepository dishRepository,
                           ExceptionHandler exceptionHandler,
                           UserService userService,
                           RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.exceptionHandler = exceptionHandler;
        this.userService = userService;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuDto addNewManu(MenuDto menuDto, double lat, double lon) {
        Restaurant restaurant = restaurantRepository.findByCoordinates(lat, lon);

        if (!Objects.isNull(restaurant)) {
            Menu menu = menuRepository.getMenuByName(menuDto.getName());
            if (Objects.isNull(menu)) {
                IdUtils idUtils = new IdUtils();

                menu = new Menu()
                        .setId(idUtils.generateUuid())
                        .setMenuType(menuDto.getMenuType())
                        .setName(menuDto.getName())
                        .setDescription(menuDto.getDescription())
                        .setDu(DateUtils.todayToStr())
                        .setDc(DateUtils.todayToStr())
                        .setDishes(new HashSet<>())
                        .setReviews(new HashSet<>());

                restaurant.getMenus().add(menu);
                menuRepository.addNewManu(menu);
                restaurantRepository.updateRestaurant(restaurant);

                return MenuMapper.toMenuDto(menu);
            }
            throw exception(ModelType.MENU, ExceptionType.DUPLICATE_ENTITY);
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND);
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
    public List<MenuDto> getAllMenus() {
        List<Menu> menus = menuRepository.getAllMenus();
        return new ArrayList<>(menus
                .stream()
                .map(menu -> new ModelMapper().map(menu, MenuDto.class))
                .collect(Collectors.toList())
        );
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
