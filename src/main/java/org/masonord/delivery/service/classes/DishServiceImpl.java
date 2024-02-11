package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.DishMapper;
import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.DishRepository;
import org.masonord.delivery.repository.MenuRepository;
import org.masonord.delivery.repository.RestaurantRepository;
import org.masonord.delivery.service.interfaces.DishService;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository,
                           RestaurantRepository restaurantRepository,
                           MenuRepository menuRepository, ExceptionHandler exceptionHandler) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public DishDto addNewDish(DishDto dishDto, String menuName, double lat, double lon) {
        Restaurant restaurant = restaurantRepository.findByCoordinates(lat, lon);

        if (!Objects.isNull(restaurant)) {
            Menu menu = menuRepository.getMenuByName(menuName);
            if (!Objects.isNull(menuName)) {
                Dish dish = dishRepository.getDishByName(dishDto.getName());
                if (Objects.isNull(dish)) {
                    IdUtils idUtils = new IdUtils();

                    dish = new Dish()
                            .setId(idUtils.generateUuid())
                            .setMenu(menu)
                            .setName(dishDto.getName())
                            .setReviews(new HashSet<>())
                            .setDescription(dishDto.getDescription())
                            .setCost(dishDto.getCost())
                            .setOrderItem(dishDto.getOrderItem());

                    menu.getDishes().add(dish);
                    menuRepository.updateMenu(menu);

                    return DishMapper.toDishDto(dish);
                }
                throw exception(ModelType.MENU, ExceptionType.DUPLICATE_ENTITY);
            }
            throw exception(ModelType.MENU, ExceptionType.ENTITY_NOT_FOUND);
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND);
    }

    @Override
    public DishDto getDishByName(String name) {
        Dish dish = dishRepository.getDishByName(name);
        if (!Objects.isNull(dish)) {
            return DishMapper.toDishDto(dish);
        }
        throw exception(ModelType.DISH, ExceptionType.ENTITY_NOT_FOUND);
    }

    @Override
    public List<DishDto> getAllDishes(int offset, int limit) {
        return new ArrayList<>(dishRepository
                .getAllDishes(offset, limit)
                .stream()
                .map(dish -> new ModelMapper().map(dish, DishDto.class))
                .collect(Collectors.toList())
        );
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String... args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
