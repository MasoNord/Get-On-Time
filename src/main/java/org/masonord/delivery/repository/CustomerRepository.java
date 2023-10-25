package org.masonord.delivery.repository;

import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.model.Customer;
import java.util.List;

public interface CustomerRepository {
    Customer addCourier(CustomerDto courierDto);

    Customer getById(String id);

    List<Customer> getAll();

    void removeCourier();
}
