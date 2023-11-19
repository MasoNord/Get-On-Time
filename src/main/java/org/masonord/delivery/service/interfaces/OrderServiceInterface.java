package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Order;

import java.util.List;

public interface OrderServiceInterface {

    OrderDto getOrderById(String id);

    OrderDto addNewOrder(OrderDto orderDto, LocationDto locationDto);

    List<OrderDto> getOrders(OffsetBasedPageRequest offsetBasedPageRequest);

    OrderDto updateOrderProfile(Order order);
}
