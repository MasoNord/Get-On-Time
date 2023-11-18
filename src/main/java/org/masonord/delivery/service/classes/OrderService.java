package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.OrderMapper;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.Order;
import org.masonord.delivery.repository.dao.CustomerDao;
import org.masonord.delivery.repository.dao.OrderDao;
import org.masonord.delivery.service.interfaces.OrderServiceInterface;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service("OrderService")
public class OrderService implements OrderServiceInterface {
    @Autowired
    IdUtils idUtils;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    LocationService locationService;

    @Override
    public OrderDto getOrderById(String id) {
        Order order = orderDao.getOrder(id);

        if (order != null) {
            if (idUtils.validateUuid(id))
                 return OrderMapper.toOrderDto(order);
            throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT);
        }
        throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND, id);
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
    public List<OrderDto> getOrders() {
        List<OrderDto> orders = new LinkedList<>();
        List<Order> orderEntity = orderDao.getOrders();
        for (Order o : orderEntity)
            orders.add(OrderMapper.toOrderDto(o));

        return orders;
    }

    public List<OrderDto> getOrders(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<OrderDto> orders = new LinkedList<>();
        List<Order> orderEntity = orderDao.getOrders(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());
        for (Order o : orderEntity)
            orders.add(OrderMapper.toOrderDto(o));

        return orders;
    }
    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}