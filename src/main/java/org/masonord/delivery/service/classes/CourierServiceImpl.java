package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.CourierMapper;
import org.masonord.delivery.dto.mapper.LocationMapper;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CourierMetaInfoDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.enums.*;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service("courierService")
public class CourierServiceImpl implements org.masonord.delivery.service.interfaces.CourierService {

    private final UserRepository userRepository;
    private final LocationService locationService;
    private final CompletedOrderRepository completedOrderRepository;
    private final OrderRepository orderRepository;
    private final IdUtils idUtils;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public CourierServiceImpl(UserRepository userRepository,
                              LocationService locationService,
                              CompletedOrderRepository completedOrderRepository,
                              OrderRepository orderRepository,
                              IdUtils idUtils,
                              ExceptionHandler exceptionHandler) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.completedOrderRepository = completedOrderRepository;
        this.orderRepository = orderRepository;
        this.idUtils = idUtils;
        this.exceptionHandler = exceptionHandler;
    }

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

    // TODO: come back when restaurant will be ready

    @Override
    public CourierDto setNewOrder(String orderId, String email) {
        Order order = orderRepository.getOrder(orderId);
        User courier = userRepository.findUserByEmail(email);
        if (idUtils.validateUuid(orderId)) {
            if (!Objects.isNull(order)) {
                if (!Objects.isNull(courier)) {
                    if (Objects.isNull(order.getCourier())) {
                        Set<Order> rides = courier.getRides();

                        order.setCourier(courier);
                        order.setOrderStatusType(OrderStatusType.DRIVE);
                        rides.add(order);

                        orderRepository.updateOrderProfile(order);

                        courier.setRides(rides);
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
    public String acceptOrder(String courierEmail, String orderId) {
        return null;
    }

    @Override
    public List<OrderDto> getOrders(String email) {
        if (!Objects.isNull(email)) {
            User user = userRepository.findUserByEmail(email);
            if (!Objects.isNull(email)) {
                return new ArrayList<>(user
                        .getRides()
                        .stream()
                        .map(order -> new ModelMapper().map(order, OrderDto.class))
                        .collect(Collectors.toList())
                );
            }
            throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
        }
        throw exception(ModelType.COURIER, ExceptionType.INVALID_ARGUMENT_EXCEPTION);
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
    public User updateProfile(String id, User newUserProfile) {
        return null;
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
