package org.masonord.delivery.service.classes;

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
import org.masonord.delivery.util.DateUtils;
import org.masonord.delivery.util.IdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements org.masonord.delivery.service.interfaces.OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CompletedOrderRepository completedOrderRepository;
    private final DishRepository dishRepository;
    private final OrderItemRepository orderItemRepository;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository,
                            OrderRepository orderRepository,
                            RestaurantRepository restaurantRepository,
                            CompletedOrderRepository completedOrderRepository,
                            DishRepository dishRepository,
                            OrderItemRepository orderItemRepository,
                            ExceptionHandler exceptionHandler) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.completedOrderRepository = completedOrderRepository;
        this.dishRepository = dishRepository;
        this.orderItemRepository = orderItemRepository;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public OrderDto getOrderById(String id) {
        Order order = orderRepository.getOrder(id);
        IdUtils idUtils = new IdUtils();

        if (idUtils.validateUuid(id)) {
            if (!Objects.isNull(order)) {
                return OrderMapper.toOrderDto(order);
            }
            throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND);
        }
        throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, id);
    }

    @Override
    public OrderDto addNewOrder(String restaurantName, String customerName, List<String> dishes) {
        Restaurant restaurant = restaurantRepository.getRestaurant(restaurantName);
        User currCustomer = userRepository.findUserByEmail(customerName);

        if (!Objects.isNull(restaurant)) {
            float cost = 0.0f;
            OrderItem items = new OrderItem()
                    .setId(UUID.randomUUID().toString())
                    .setDishes(new HashSet<>());

            for (String name : dishes) {
                Dish tempDish = dishRepository.getDishByName(name);
                if (!Objects.isNull(tempDish)) {
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
    public List<OrderDto> getOrders(int offset, int limit) {
        return new ArrayList<>(orderRepository
                .getOrders(offset, limit)
                .stream()
                .map(order -> new ModelMapper().map(order, OrderDto.class))
                .collect(Collectors.toList())
        );
    }

    @Override
    public String changeOrderStatus(String orderId, String restaurantName, OrderStatusType status) {
        Restaurant restaurant = restaurantRepository.getRestaurant(restaurantName);
        IdUtils idUtils = new IdUtils();
        if (!Objects.isNull(restaurant)) {
            if (idUtils.validateUuid(orderId)) {
                Order order = orderRepository.getOrder(orderId);
                if (!Objects.isNull(order)) {
                    order.setOrderStatusType(status);
                    orderRepository.updateOrderProfile(order);
                    return "\"message\": \"Order's status has been updated successfully\"";
                }
                throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND, orderId);
            }
            throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, orderId);
        }
        throw exception(ModelType.RESTAURANT, ExceptionType.ENTITY_NOT_FOUND, restaurantName);
    }

    @Override
    public CompletedOrderDto completeOrder(String email, String orderId) {
        IdUtils idUtils = new IdUtils();

        if (idUtils.validateUuid(orderId)) {
            Order order = orderRepository.getOrder(orderId);
            User courier = userRepository.findUserByEmail(email);
            if (!Objects.isNull(courier)) {
                if (!Objects.isNull(order)) {
                    if (hasOrder(courier.getRides(), order)) {

                        CompletedOrder completedOrder = new CompletedOrder()
                                .setOrderId(order.getId())
                                .setCourier(courier)
                                .setCost(order.getCost())
                                .setCompletedTime(DateUtils.todayToStr());

                        orderRepository.deleteOrder(order);

                        return CompletedOrderMapper.toCompletedOrderDto(completedOrderRepository.addOrder(completedOrder));
                    }
                    throw exception(ModelType.COURIER, ExceptionType.CONFLICT_EXCEPTION, orderId);
                }
                throw exception(ModelType.ORDER, ExceptionType.ENTITY_NOT_FOUND,  orderId);
            }
            throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, email);
        }
        throw exception(ModelType.ORDER, ExceptionType.NOT_UUID_FORMAT, orderId);
    }

    @Override
    public OrderDto updateOrderProfile(Order order) {
        return OrderMapper.toOrderDto(orderRepository.updateOrderProfile(order));
    }

    @Override
    public String deleteOrder(String orderId) {
        Order order = orderRepository.getOrder(orderId);
        orderRepository.deleteOrder(order);
        return "\"message\": \"Order has been successfully deleted\"";
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }

    private boolean hasOrder(Set<Order> orders, Order order){
        for (Order o : orders) {
            if (Objects.equals(o.getId(), order.getId())) {
                return true;
            }
        }
        return false;
    }
}
