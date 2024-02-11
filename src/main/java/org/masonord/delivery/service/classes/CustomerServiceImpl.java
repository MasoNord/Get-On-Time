package org.masonord.delivery.service.classes;

import org.masonord.delivery.dto.mapper.CustomerMapper;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements org.masonord.delivery.service.interfaces.CustomerService {

    private final UserRepository userRepository;
    private final LocationServiceImpl locationService;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public CustomerServiceImpl(UserRepository userRepository,
                               LocationServiceImpl locationService,
                               ExceptionHandler exceptionHandler) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public CustomerDto findCustomerByEmail(String email) {
        if (!Objects.isNull(email)) {
            User customer = userRepository.findUserByEmail(email);
            if (customer != null && Objects.equals(customer.getRole(), UserRoles.CUSTOMER)) {
                return CustomerMapper.toCustomerDto(userRepository.findUserByEmail(email));
            }
            throw exception(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email);
        }

        throw exception(ModelType.CUSTOMER, ExceptionType.INVALID_ARGUMENT_EXCEPTION);
    }

    @Override
    public List<CustomerDto> getCustomers(int offset, int limit) {
        List<CustomerDto> customers = new LinkedList<>();
        List<User> customerEntity = userRepository.getAllUsers(offset, limit);

        for (User c : customerEntity) {
            if (Objects.equals(c.getRole(), UserRoles.CUSTOMER)) {
                customers.add(CustomerMapper.toCustomerDto(c));
            }
        }

        return customers;
    }

    @Override
    public List<OrderDto> getActiveOrders(String email) {
        if (!Objects.isNull(email)) {
            User user = userRepository.findUserByEmail(email);
            if (!Objects.isNull(user)) {
                return new ArrayList<>(
                        user.getOrders()
                                .stream()
                                .map(order -> new ModelMapper().map(order, OrderDto.class))
                                .collect(Collectors.toList())
                );
            }
            throw exception(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email);
        }
        throw exception(ModelType.CUSTOMER, ExceptionType.INVALID_ARGUMENT_EXCEPTION);
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
