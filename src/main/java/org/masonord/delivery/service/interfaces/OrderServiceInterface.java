package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;

import java.util.List;

public interface OrderServiceInterface {

    OrderDto getOrderById(String id);

    OrderDto addNewOrder(OrderDto orderDto, LocationDto locationDto);

    List<OrderDto> getOrders();

}
