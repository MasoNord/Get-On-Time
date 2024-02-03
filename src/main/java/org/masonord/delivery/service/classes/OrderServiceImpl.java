package org.masonord.delivery.service.classes;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.dto.mapper.CompletedOrderMapper;
import org.masonord.delivery.dto.mapper.OrderMapper;
import org.masonord.delivery.dto.model.*;
import org.masonord.delivery.enums.CourierType;
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
import org.masonord.delivery.service.interfaces.LocationService;
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("OrderService")
public class OrderServiceImpl implements org.masonord.delivery.service.interfaces.OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    CompletedOrderRepository completedOrderRepository;

    @Autowired
    DishRepository dishRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ExceptionHandler exceptionHandler;


    @Override
    public OrderDto getOrderById(String id) {
        Order order = orderRepository.getOrder(id);
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
        Restaurant restaurant = restaurantRepository.getRestaurant(restaurantName);
        User currCustomer = userRepository.findUserByEmail(customerName);

        if (restaurant != null) {
            float cost = 0.0f;
            OrderItem items = new OrderItem()
                    .setId(UUID.randomUUID().toString())
                    .setDishes(new HashSet<>());

            for (String name : dishes) {
                Dish tempDish = dishRepository.getDishByName(name);
                if (tempDish != null) {
                    items.getDishes().add(tempDish);
                    cost+= tempDish.getCost();
                }
            }
            orderItemRepository.addNewOrderItem(items);
            Order newOrder = new Order()
                    .setOrderItems(items)
                    .setId(UUID.randomUUID().toString())
                    .setOrderStatusType(OrderStatusType.PENDING)
                    .setCustomer(currCustomer)
                    .setRestaurant(restaurant)
                    .setCost(cost);
            return OrderMapper.toOrderDto(orderRepository.createOrder(newOrder));
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND, restaurantName);
    }
    @Override
    public List<OrderDto> getOrders(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<OrderDto> orders = new LinkedList<>();
        List<Order> orderEntity = orderRepository.getOrders(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());
        for (Order o : orderEntity)
            orders.add(OrderMapper.toOrderDto(o));

        return orders;

        // TODO: do refactor of a code, from for loop to lambdas
    }

    @Override
    public List<OrderDto> getClosestOrders(String courierEmail) {
        User user = userRepository.findUserByEmail(courierEmail);
        List<Order> orders = orderRepository.getOrders();
        List<OrderDto> closestOrders = new ArrayList<>();
        if (user.getLocation() != null) {
            for (Order order : orders) {
                double distance = LocationService.getDistanceFromLatLonInM(
                        user.getLocation().getLatitude(), user.getLocation().getLongitude(),
                        order.getRestaurant().getLocation().getLatitude(), order.getRestaurant().getLocation().getLongitude()
                );
                if (CourierType.WALK.equals(user.getTransport()) && distance <= 2500) {
                    closestOrders.add(OrderMapper.toOrderDto(order));
                }else if (CourierType.WALK.equals(user.getTransport()) && distance <= 4000) {
                    closestOrders.add(OrderMapper.toOrderDto(order));
                }else if (CourierType.WALK.equals(user.getTransport()) && distance <= 10000) {
                    closestOrders.add(OrderMapper.toOrderDto(order));
                }
            }

            return closestOrders;
        }

        throw exception(ModelType.USER, ExceptionType.LOCATION_NOT_SET);
    }

    @Override
    public String changeOrderStatus(String orderId, String restaurantName, OrderStatusType status) {
        Restaurant restaurant = restaurantRepository.getRestaurant(restaurantName);
        IdUtils idUtils = new IdUtils();
        if (restaurant != null) {
            if (idUtils.validateUuid(orderId)) {
                Order order = orderRepository.getOrder(orderId);
                if (order != null) {
                    order.setOrderStatusType(status);
                    orderRepository.updateOrderProfile(order);
                    return "Order has been updated successfully";
                }
                throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND, orderId);
            }
            throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, orderId);
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND, restaurantName);
    }

    @Override
    public CompletedOrderDto completeOrder(OrderCompleteRequest orderCompleteRequest) {
        User courier = userRepository.findUserByEmail(orderCompleteRequest.getCourierEmail());
        Order order = orderRepository.getOrder(orderCompleteRequest.getOrderId());
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

                        orderRepository.deleteOrder(orderRepository.getOrder(orderCompleteRequest.getOrderId()));

                        return CompletedOrderMapper.toCompletedOrderDto(completedOrderRepository.addOrder(completedOrder));
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
        return OrderMapper.toOrderDto(orderRepository.updateOrderProfile(order));
    }

    @Override
    public String deleteOrder(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        orderRepository.deleteOrder(order);
        return "Successfully destroyed";
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }

    private boolean hasOrder(Set<Order> orders, Order order){
        for (Order o : orders)
            if (Objects.equals(o.getId(), order.getId()))
                return true;
        return false;
    }

}
