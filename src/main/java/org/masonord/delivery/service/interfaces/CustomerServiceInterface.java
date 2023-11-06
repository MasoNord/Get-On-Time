package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.CustomerDto;

import java.util.List;

public interface CustomerServiceInterface {

    CustomerDto addNewCustomer(CustomerDto customerDto);

    CustomerDto findCustomerByEmail(String email);

    CustomerDto findCustomerById(Long id);

    List<CustomerDto> getCustomers();

    CustomerDto updateCustomer(String email, CustomerDto newCostumerProfile);

    void deleteCustomer(String email);
}
