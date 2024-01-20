package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.DishDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.dto.model.OrderItemDto;
import org.masonord.delivery.model.order.Order;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setCost(order.getCost())
                .setStatus(order.getOrderStatusType())
                .setCourierEmail(order.getCourier() == null ? "NONE" : order.getCourier().getEmail())
                .setCustomerEmail(order.getCustomer().getEmail())
                .setItems(new HashSet<>(order.getOrderItems().getDishes())
                        .stream()
                        .map(dish -> new ModelMapper().map(dish, DishDto.class))
                        .collect(Collectors.toSet())
                );
    }
}
