package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.dto.mapper.CompletedOrderMapper;
import org.masonord.delivery.dto.mapper.OrderMapper;
import org.masonord.delivery.dto.model.*;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.OrderStatusType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.*;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.model.restarurant.Dish;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.repository.*;
import org.masonord.delivery.repository.hibernate.*;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("OrderService")
public class OrderServiceImpl implements org.masonord.delivery.service.interfaces.OrderService {

    @Autowired
    UserRep userRep;

    @Autowired
    OrderRep orderRep;

    @Autowired
    RestaurantRep restaurantRep;

    @Autowired
    CompletedOrderRep completedOrderRep;

    @Autowired
    DishRep dishRep;

    @Autowired
    OrderItemRep orderItemRep;


    @Override
    public OrderDto getOrderById(String id) {
        Order order = orderRep.getOrder(id);
        IdUtils idUtils = new IdUtils();

        if (idUtils.validateUuid(id)) {
            if (order != null)
                 return OrderMapper.toOrderDto(order);
            throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND);
        }
        throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, id);
    }
    @Override
    public OrderDto addNewOrder(String restaurantName, String customerName, List<String> dishes) {
        Restaurant restaurant = restaurantRep.getRestaurant(restaurantName);
        User currCustomer = userRep.findUserByEmail(customerName);

        if (restaurant != null) {
            float cost = 0.0f;
            OrderItem items = new OrderItem()
                    .setId(UUID.randomUUID().toString())
                    .setDishes(new HashSet<>());

            for (String name : dishes) {
                Dish tempDish = dishRep.getDishByName(name);
                if (tempDish != null) {
                    items.getDishes().add(tempDish);
                    cost+= tempDish.getCost();
                }
            }
            orderItemRep.addNewOrderItem(items);
            Order newOrder = new Order()
                    .setOrderItems(items)
                    .setId(UUID.randomUUID().toString())
                    .setOrderStatusType(OrderStatusType.PENDING)
                    .setCustomer(currCustomer)
                    .setRestaurant(restaurant)
                    .setCost(cost);
            return OrderMapper.toOrderDto(orderRep.createOrder(newOrder));
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND, restaurantName);
    }
    @Override
    public List<OrderDto> getOrders(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<OrderDto> orders = new LinkedList<>();
        List<Order> orderEntity = orderRep.getOrders(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());
        for (Order o : orderEntity)
            orders.add(OrderMapper.toOrderDto(o));

        return orders;

        // TODO: do refactor of a code, from for loop to lambdas
    }

    @Override
    public CompletedOrderDto completeOrder(OrderCompleteRequest orderCompleteRequest) {
        User courier = userRep.findUserByEmail(orderCompleteRequest.getCourierEmail());
        Order order = orderRep.getOrder(orderCompleteRequest.getOrderId());
        IdUtils idUtils = new IdUtils();

        if (idUtils.validateUuid(orderCompleteRequest.getOrderId())) {
            if (courier != null) {
                if (order != null) {
                    if (hasOrder(courier.getOrders(), order)) {

                        CompletedOrder completedOrder = new CompletedOrder()
                                .setOrderId(order.getId())
                                .setCourier(courier)
                                .setCost(order.getCost())
                                .setCompletedTime(DateUtils.todayToStr());

                        orderRep.deleteOrder(orderRep.getOrder(orderCompleteRequest.getOrderId()));

                        return CompletedOrderMapper.toCompletedOrderDto(completedOrderRep.addOrder(completedOrder));
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
        return OrderMapper.toOrderDto(orderRep.updateOrderProfile(order));
    }

    @Override
    public String deleteOrder(String orderId) {
        Order order = orderRep.getOrder(orderId);
        orderRep.deleteOrder(order);
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
