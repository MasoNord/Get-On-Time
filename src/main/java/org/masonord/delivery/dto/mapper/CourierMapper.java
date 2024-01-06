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
                .setOrders(new HashSet<OrderDto>(courier
                        .getOrders()
                        .stream()
                        .map(order -> new ModelMapper().map(order, OrderDto.class))
                        .collect(Collectors.toSet())
                ));
    }
    //TODO: come back later when I  have a clear plan for courier's rating system

//    public static CourierMetaInfoDto toCourierMetaInfoDto(User courier) {
//        return new CourierMetaInfoDto()
//                .setEarnings(courier.getEarnings())
//                .setRating(courier.getRating())
//                .setLocationDto(LocationMapper.toLocationDto(courier.getLocation()))
//                .setWorkingHours(courier.getWorkingHours());
//    }
}
