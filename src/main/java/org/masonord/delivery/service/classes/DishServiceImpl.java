package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.repository.DishRep;
import org.masonord.delivery.service.interfaces.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DishService")
public class DishServiceImpl implements DishService {

    @Autowired
    DishRep dishRep;

    @Override
    public DishDto addNewDish(DishDto dishDto) {
        return null;
    }

    @Override
    public DishDto getDishByName(String name) {
        return null;
    }

    @Override
    public DishDto updateDishRecord(String name) {
        return null;
    }

    @Override
    public List<DishDto> getAllDishes() {
        return null;
    }

    @Override
    public void deleteDish(String name) {

    }
}
