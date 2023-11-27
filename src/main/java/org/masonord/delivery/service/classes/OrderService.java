package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.dto.mapper.CompletedOrderMapper;
import org.masonord.delivery.dto.mapper.OrderMapper;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.*;
import org.masonord.delivery.repository.dao.CompletedOrderDao;
import org.masonord.delivery.repository.dao.CourierDao;
import org.masonord.delivery.repository.dao.CustomerDao;
import org.masonord.delivery.repository.dao.OrderDao;
import org.masonord.delivery.service.interfaces.OrderServiceInterface;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service("OrderService")
public class OrderService implements OrderServiceInterface {
    @Autowired
    IdUtils idUtils;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    CourierDao courierDao;

    @Autowired
    CompletedOrderDao completedOrderDao;

    @Autowired
    LocationService locationService;

    @Override
    public OrderDto getOrderById(String id) {
        Order order = orderDao.getOrder(id);

        if (idUtils.validateUuid(id)) {
            if (order != null)
                 return OrderMapper.toOrderDto(order);
            throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND);
        }
        throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, id);
    }
    @Override
    public OrderDto addNewOrder(OrderDto orderDto, LocationDto locationDto) {
        Location newLocation = locationService.addNewPlaceByName(locationDto);

        Order order = new Order()
                .setId(idUtils.generateUuid())
                .setLocation(newLocation)
                .setCost(orderDto.getCost())
                .setWeight(orderDto.getWeight())
                .setDeliveryHours(orderDto.getDeliveryHours())
                .setCustomer(customerDao.getCustomerByEmail(orderDto.getCustomerEmail()));
        return OrderMapper.toOrderDto(orderDao.createOrder(order));
    }
    @Override
    public List<OrderDto> getOrders(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<OrderDto> orders = new LinkedList<>();
        List<Order> orderEntity = orderDao.getOrders(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());
        for (Order o : orderEntity)
            orders.add(OrderMapper.toOrderDto(o));

        return orders;
    }

    @Override
    public CompletedOrderDto completeOrder(OrderCompleteRequest orderCompleteRequest) {
        Courier courier = courierDao.getCourierByEmail(orderCompleteRequest.getCourierEmail());
        Order order = orderDao.getOrder(orderCompleteRequest.getOrderId());

        if (idUtils.validateUuid(orderCompleteRequest.getOrderId())) {
            if (courier != null) {
                if (order != null) {
                    if (hasOrder(courier.getOrders(), order)) {

                        CompletedOrder completedOrder = new CompletedOrder()
                                .setOrderId(order.getId())
                                .setCourier(courier)
                                .setCost(order.getCost())
                                .setCompletedTime(DateUtils.todayToStr());

                        orderDao.deleteOrder(orderDao.getOrder(orderCompleteRequest.getOrderId()));

                        return CompletedOrderMapper.toCompletedOrderDto(completedOrderDao.addOrder(completedOrder));
                    }
                    throw exception(ModelType.COURIER, ExceptionType.CONFLICT_EXCEPTION, orderCompleteRequest.getOrderId());
                }
                throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND, orderCompleteRequest.getOrderId());
            }
            throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, orderCompleteRequest.getCourierEmail());
        }
        throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, orderCompleteRequest.getOrderId());
    }

    @Override
    public OrderDto updateOrderProfile(Order order) {
        return OrderMapper.toOrderDto(orderDao.updateOrderProfile(order));
    }

    @Override
    public String deleteOrder(String orderId) {
        Order order = orderDao.getOrder(orderId);
        orderDao.deleteOrder(order);
        return "Successfully destroyed";
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }

    private boolean hasOrder(Set<Order> orders, Order order){
        for (Order o : orders)
            if (Objects.equals(o.getId(), order.getId()))
                return true;
        return false;
    }

}
