package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.model.Customer;
import java.util.List;

public interface CustomerDaoInterface {
    Customer createCustomer(Customer Customer);

    Customer getCustomer(String id);

    List<Customer> getCustomers();

    void deleteCustomer(String id);
}
