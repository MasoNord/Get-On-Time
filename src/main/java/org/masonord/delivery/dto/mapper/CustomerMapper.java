package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.Customer;
import org.masonord.delivery.model.Order;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto()
                .setEmail(customer.getEmail())
                .setLastName(customer.getLastName())
                .setFirstName(customer.getFirstName())
                .setOrders(new HashSet<>(customer
                        .getOrders()
                        .stream()
                        .map(order -> new ModelMapper().map(order, OrderDto.class))
                        .collect(Collectors.toSet())
                ));
    }
}
