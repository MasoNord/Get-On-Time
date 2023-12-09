package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.order.Order;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setCost(order.getCost())
                .setWeight(order.getWeight())
                .setCourierEmail(order.getCourier().getEmail())
                .setCustomerEmail(order.getCustomer().getEmail())
                .setDeliveryHours(order.getDeliveryHours());
    }
}
