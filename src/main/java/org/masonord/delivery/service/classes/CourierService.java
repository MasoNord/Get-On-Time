package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.CourierMapper;
import org.masonord.delivery.dto.mapper.LocationMapper;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CourierMetaInfoDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.CourierCriteriaType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.model.Courier;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.Order;
import org.masonord.delivery.repository.dao.CompletedOrderDao;
import org.masonord.delivery.repository.dao.CourierDao;
import org.masonord.delivery.repository.dao.OrderDao;
import org.masonord.delivery.service.interfaces.CourierServiceInterface;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

@Service("courierService")
public class CourierService implements CourierServiceInterface {

    @Autowired
    private CourierDao courierDao;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CompletedOrderDao completedOrderDao;

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
                .setOrders(emptyOrderSet)
                .setEarnings(0.0f)
                .setRating(0.0f);

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
    public List<CourierDto> getAllCouriers(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<CourierDto> couriers = new LinkedList<>();
        List<Courier> couriersEntity = courierDao.getCouriers(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());

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
    public CourierDto setNewOrder(String orderId, String email) {
        Order order = orderDao.getOrder(orderId);
        Courier courier = courierDao.getCourierByEmail(email);
        if (idUtils.validateUuid(orderId)) {
            if (order != null) {
                if (courier != null) {
                    if (order.getCourier() == null) {
                        Set<Order> orders = courier.getOrders();

                        order.setCourier(courier);
                        orders.add(order);
                        orderDao.updateOrderProfile(order);

                        courier.setOrders(orders);
                        return CourierMapper.toCourierDto(courierDao.updateProfile(courier));
                    }
                    throw exception(ModelType.ORDER, ExceptionType.CONFLICT_EXCEPTION, orderId);
                }
                throw exception (ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
            }
            throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND, orderId);
        }
        throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, orderId);
    }

    @Override
    public void calculateRatingAndSalary(String courierEmail, Order order) {
        Courier courier = courierDao.getCourierByEmail(courierEmail);
//        List<CompletedOrder> completedOrders = completedOrderDao.getCompletedOrderByEmail(courierEmail);

        if (courier != null) {
            int criteria = (CourierCriteriaType.valueOf(courier.getTransport().getValue()).getValue());
            float newSalary = courier.getEarnings() + (order.getCost() * criteria);
            float newRating = courier.getRating() + 1;
        }

        throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, courierEmail);
    }

    @Override
    public CourierMetaInfoDto getMetaInfo(String courierEmail, String startDate, String endDate) throws ParseException {
        Courier courier = courierDao.getCourierByEmail(courierEmail);

        if (courier != null) {
            List<CompletedOrder> completedOrders = completedOrderDao.getCompletedOrders();
            int criteria = (CourierCriteriaType.valueOf(courier.getTransport().getValue().toUpperCase()).getValue());
            int countCompletedOrders = 0;
            float rating = 0.0f, earnings = 0.0f;

            for (CompletedOrder o : completedOrders) {
                if (DateUtils.DetermineDateBetweenTwoDates(startDate, endDate, o.getCompletedTime()) && Objects.equals(o.getCourier().getEmail(), courier.getEmail())) {
                    earnings += o.getCost() * criteria;
                    countCompletedOrders++;
                }
            }

            rating = ((float) countCompletedOrders / DateUtils.DifferenceBetweenTwoDates(startDate, endDate)) * criteria;

            return new CourierMetaInfoDto()
                    .setLocationDto(LocationMapper.toLocationDto(courier.getLocation()))
                    .setRating(rating)
                    .setWorkingHours(courier.getWorkingHours())
                    .setEarnings(earnings);
        }

        throw  exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, courierEmail);
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
