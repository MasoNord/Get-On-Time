package org.masonord.delivery.service.classes;


import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.mapper.CourierMapper;
import org.masonord.delivery.dto.mapper.CustomerMapper;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.Courier;
import org.masonord.delivery.model.Customer;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.Order;
import org.masonord.delivery.repository.dao.CourierDao;
import org.masonord.delivery.repository.dao.CustomerDao;
import org.masonord.delivery.service.interfaces.CustomerServiceInterface;
import org.masonord.delivery.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service("CustomerService")
public class CustomerService implements CustomerServiceInterface {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    LocationService locationService;

    @Override
    public CustomerDto addNewCustomer(CustomerDto customerDto) {
        Set<Order> emptyOrderList = new HashSet<>();

        Customer customer = new Customer()
                .setEmail(customerDto.getEmail())
                .setFirstName(customerDto.getFirstName())
                .setLastName(customerDto.getLastName())
                .setDu(DateUtils.todayToStr())
                .setDc(DateUtils.todayToStr())
                .setOrders(emptyOrderList);

        return CustomerMapper.toCustomerDto(customerDao.createCustomer(customer));
    }

    @Override
    public CustomerDto findCustomerByEmail(String email) {
        return CustomerMapper.toCustomerDto(customerDao.getCustomerByEmail(email));
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        return CustomerMapper.toCustomerDto(customerDao.getCustomerById(id));
    }

    @Override
    public List<CustomerDto> getCustomers() {
        List<CustomerDto> customers = new LinkedList<>();
        List<Customer> customerEntity = customerDao.getCustomers();
        for (Customer c : customerEntity) {
            customers.add(CustomerMapper.toCustomerDto(c));
        }

        return customers;
    }

    public List<CustomerDto> getCustomers(OffsetBasedPageRequest offsetBasedPageRequest) {
        List<CustomerDto> customers = new LinkedList<>();
        List<Customer> customerEntity = customerDao.getCustomers(offsetBasedPageRequest.getOffset(), offsetBasedPageRequest.getPageSize());

        for (Customer c : customerEntity) {
            customers.add(CustomerMapper.toCustomerDto(c));
        }

        return customers;
    }

    @Override
    public CustomerDto updateCustomer(String email, CustomerDto newCostumerProfile) {
        return null;
    }

    @Override
    public String updateCurrentLocation(LocationDto locationDto, String email) {
        Customer customer = customerDao.getCustomerByEmail(email);
        if (customer != null) {
            Location location = locationService.addNewPlaceByName(locationDto);
            customer.setLocation(location);
            customerDao.updateProfile(customer);

            return "The location has been successfully updated";
        }

        throw exception(ModelType.CUSTOMER, ExceptionType.ENTITY_NOT_FOUND, email);

    }
    @Override
    public void deleteCustomer(String email) {

    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
