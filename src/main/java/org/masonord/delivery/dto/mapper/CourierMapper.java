package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.User;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.stream.Collectors;
public class CourierMapper {
    public static CourierDto toCourierDto(User courier) {
        return new CourierDto()
                .setEmail(courier.getEmail())
                .setFirstName(courier.getFirstName())
                .setLastName(courier.getLastName())
                .setTransport(courier.getTransport())
                .setWorkingHours(courier.getWorkingHours())
                .setRides(new HashSet<OrderDto>(courier
                        .getRides()
                        .stream()
                        .map(order -> new ModelMapper().map(order, OrderDto.class))
                        .collect(Collectors.toSet())
                ));
    }
}
