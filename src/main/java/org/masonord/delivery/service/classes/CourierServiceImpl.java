package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.CourierMapper;
import org.masonord.delivery.dto.mapper.LocationMapper;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CourierMetaInfoDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.CourierCriteriaType;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.repository.CompletedOrderRepository;
import org.masonord.delivery.repository.OrderRepository;
import org.masonord.delivery.repository.UserRepository;
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service("courierService")
public class CourierServiceImpl implements org.masonord.delivery.service.interfaces.CourierService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private CompletedOrderRepository completedOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IdUtils idUtils;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Override
    public CourierDto findCourierByEmail(String email) {
        User courier = userRepository.findUserByEmail(email);

        if (courier != null && Objects.equals(courier.getRole(), UserRoles.COURIER)) {
            return CourierMapper.toCourierDto(courier);
        }

        throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public List<CourierDto> getAllCouriers(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<CourierDto> couriers = new LinkedList<>();
        List<User> couriersEntity = userRepository.getAllUsers(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());

        for (User c : couriersEntity) {
            if (Objects.equals(c.getRole(), UserRoles.COURIER)) {
                couriers.add(CourierMapper.toCourierDto(c));
            }
        }

        return couriers;
    }

    @Override
    public String updateCurrentLocation(LocationDto locationDto, String email) {
       User courier = userRepository.findUserByEmail(email);

       if (courier != null && Objects.equals(courier.getRole(), UserRoles.COURIER)) {
            Location location = locationService.addNewPlaceByName(locationDto);
            courier.setLocation(location);
            userRepository.updateUserProfile(courier);

            return "The location has been successfully updated";
       }
       throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    // TODO: come back when restaurant will be ready

    @Override
    public CourierDto setNewOrder(String orderId, String email) {
        Order order = orderRepository.getOrder(orderId);
        User courier = userRepository.findUserByEmail(email);
        if (idUtils.validateUuid(orderId)) {
            if (order != null) {
                if (courier != null && Objects.equals(courier.getRole(), UserRoles.COURIER)) {
                    if (order.getCourier() == null && Objects.equals(order.getCustomer().getRole(), UserRoles.COURIER)) {
                        Set<Order> orders = courier.getOrders();

                        order.setCourier(courier);
                        orders.add(order);
                        orderRepository.updateOrderProfile(order);

                        courier.setOrders(orders);
                        return CourierMapper.toCourierDto(userRepository.updateUserProfile(courier));
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
    }

    @Override
    public CourierMetaInfoDto getMetaInfo(String courierEmail, String startDate, String endDate) throws ParseException {
        User courier = userRepository.findUserByEmail(courierEmail);

        if (courier != null) {
            List<CompletedOrder> completedOrders = completedOrderRepository.getCompletedOrders();
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
    public String acceptOrder(String courierEmail, String orderId) {
        return null;
    }

    @Override
    public User updateProfile(String id, User newUserProfile) {
        return null;
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
