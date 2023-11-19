package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Order;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        String email = "";

        if(order.getCourier() != null)
            email = order.getCourier().getEmail();

        return new OrderDto()
                .setId(order.getId())
                .setCost(order.getCost())
                .setWeight(order.getWeight())
                .setCourierEmail(email)
                .setCustomerEmail(order.getCustomer().getEmail())
                .setDeliveryHours(order.getDeliveryHours());
    }
}
