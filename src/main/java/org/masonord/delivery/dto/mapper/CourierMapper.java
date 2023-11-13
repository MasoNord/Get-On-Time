package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Courier;
import org.masonord.delivery.model.Order;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.stream.Collectors;

public class CourierMapper {
    public static CourierDto toCourierDto(Courier courier) {
        return new CourierDto()
                .setEmail(courier.getEmail())
                .setFirstName(courier.getFirstName())
                .setLastName(courier.getLastName())
                .setTransport(courier.getTransport())
                .setWorkingHours(courier.getWorkingHours())
                .setOrders(new HashSet<Order>(courier
                        .getOrders()
                        .stream()
                        .map(order -> new ModelMapper().map(order, Order.class))
                        .collect(Collectors.toSet())
                ));
    }
}
