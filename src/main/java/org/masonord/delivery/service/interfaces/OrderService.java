package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.controller.v1.request.OrderCompleteRequest;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
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
     * @param orderDto
     * @param locationDto
     * @return OrderDto
     */

    OrderDto addNewOrder(OrderDto orderDto, LocationDto locationDto);

    /**
     * Get list of all orders
     *
     * @param offsetBasedPageRequest - contain offset and limit parameters to apply pagination
     * @return List<OrderDto>
     */

    List<OrderDto> getOrders(OffsetBasedPageRequest offsetBasedPageRequest);

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
}
