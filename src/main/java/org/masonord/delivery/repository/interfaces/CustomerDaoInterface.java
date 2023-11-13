package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Customer;
import java.util.List;

public interface CustomerDaoInterface {
    Customer createCustomer(Customer Customer);

    Customer getCustomerById(Long id);

    Customer getCustomerByEmail(String email);

    List<Customer> getCustomers();

    void deleteCustomer(Long id);

     Customer updateProfile(Customer customer);
}
