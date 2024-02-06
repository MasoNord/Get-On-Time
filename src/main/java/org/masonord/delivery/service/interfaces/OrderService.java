package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.enums.OrderStatusType;
import org.masonord.delivery.model.order.Order;

import java.util.List;

public interface OrderService {
    /**
     * Get an order by the given id
     *
     * @param id
     * @return OrderDto
     */

    OrderDto getOrderById(String id);

    /**
     * Add a new order to a database
     *
     * @param customerName
     * @return OrderDto
     */

    OrderDto addNewOrder(String restaurantName, String customerName, List<String> dishes);

    /**
     * Get list of all orders
     *
     * @param offset
     * @param limit
     * @return List<OrderDto>
     */

    List<OrderDto> getOrders(int offset, int limit);


    /**
     * Complete an order when the ride comes to the end
     *
     * @param orderCompleteRequest
     * @return CompletedOrderDto
     */

    CompletedOrderDto completeOrder(OrderCompleteRequest orderCompleteRequest);

    /**
     * update an existing order until it's not on a ride
     *
     * @param order
     * @return OrderDto
     */

    OrderDto updateOrderProfile(Order order);

    /**
     * delete an order's record by his ID and return a corresponding message
     *
     * @param orderId
     * @return String
     */

    String deleteOrder(String orderId);


    String changeOrderStatus(String orderId, String restaurantName, OrderStatusType status);
}
