package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Order;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setCost(order.getCost())
                .setWeight(order.getWeight())
                .setCourier(order.getCourier())
                .setCustomer(order.getCustomer())
                .setDeliveryHours(order.getDeliveryHours());
    }
}
