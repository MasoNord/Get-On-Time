package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.CourierMapper;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.UserDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Courier;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.Order;
import org.masonord.delivery.repository.dao.CourierDao;
import org.masonord.delivery.repository.dao.LocationDao;
import org.masonord.delivery.repository.dao.OrderDao;
import org.masonord.delivery.service.interfaces.CourierServiceInterface;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service("courierService")
public class CourierService implements CourierServiceInterface {

    @Autowired
    private CourierDao courierDao;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private IdUtils idUtils;


    @Override
    public CourierDto addNewCourier(CourierDto courierDto) {
        Set<Order> emptyOrderSet = new HashSet<>();

        Courier courier = new Courier()
                .setDc(DateUtils.todayToStr())
                .setDu(DateUtils.todayToStr())
                .setEmail(courierDto.getEmail())
                .setFirstName(courierDto.getFirstName())
                .setLastName(courierDto.getLastName())
                .setTransport(courierDto.getTransport())
                .setWorkingHours(courierDto.getWorkingHours())
                .setOrders(emptyOrderSet);

        return CourierMapper.toCourierDto(courierDao.createCourier(courier));
    }

    @Override
    public CourierDto findCourierById(Long id) {
        return CourierMapper.toCourierDto(courierDao.getCourierById(id));
    }

    @Override
    public CourierDto findCourierByEmail(String email) {
        Courier courier = courierDao.getCourierByEmail(email);

        if (courier != null) {
            return CourierMapper.toCourierDto(courierDao.getCourierByEmail(email));
        }

        throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public List<CourierDto> getAllCouriers() {
        List<CourierDto> couriers = new LinkedList<>();
        List<Courier> couriersEntity = courierDao.getCouriers();
        for (Courier c : couriersEntity) {
            couriers.add(CourierMapper.toCourierDto(c));
        }

        return couriers;
    }
    @Override
    public String updateCurrentLocation(LocationDto locationDto, String email) {
       Courier courier = courierDao.getCourierByEmail(email);

       if (courier != null) {
            Location location = locationService.addNewPlaceByName(locationDto);
            courier.setLocation(location);
            courierDao.updateProfile(courier);

            return "The location has been successfully updated";
       }
       throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
    }
    @Override
    public void DeleteCourierById(Long id) {

    }

    @Override
    public Courier updateProfile(String id, Courier newUserProfile) {
        return null;
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
