package org.masonord.delivery.service.classes;


import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.CustomerMapper;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service("CustomerService")
public class CustomerServiceImpl implements org.masonord.delivery.service.interfaces.CustomerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationServiceImpl locationService;

    @Autowired
    ExceptionHandler exceptionHandler;

    @Override
    public CustomerDto findCustomerByEmail(String email) {
        User customer = userRepository.findUserByEmail(email);
        if (customer != null && Objects.equals(customer.getRole(), UserRoles.CUSTOMER)) {
            return CustomerMapper.toCustomerDto(userRepository.findUserByEmail(email));
        }
        throw exception(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email);
    }

    @Override
    public List<CustomerDto> getCustomers(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<CustomerDto> customers = new LinkedList<>();
        List<User> customerEntity = userRepository.getAllUsers(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());

        for (User c : customerEntity) {
            if (Objects.equals(c.getRole(), UserRoles.CUSTOMER)) {
                customers.add(CustomerMapper.toCustomerDto(c));
            }
        }

        return customers;
    }

    @Override
    public CustomerDto updateCustomer(String email, CustomerDto newCostumerProfile) {
        return null;
    }

    @Override
    public String updateCurrentLocation(LocationDto locationDto, String email) {
        User customer = userRepository.findUserByEmail(email);
        if (customer != null) {
            Location location = locationService.addNewPlaceByName(locationDto);
            customer.setLocation(location);
            userRepository.updateUserProfile(customer);

            return "The location has been successfully updated";
        }
        throw exception(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email);
    }
    @Override
    public void deleteCustomer(String email) {

    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return exceptionHandler.throwException(entity, exception, args);
    }
}
