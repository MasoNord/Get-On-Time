package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;

import java.util.List;

public interface CustomerService {

    /**
     * get a customer's record by the given id
     *
     * @param email
     * @return CustomerDto
     */

    CustomerDto findCustomerByEmail(String email);

    /**
     * get list of all customers
     *
     * @param offset
     * @param limit
     * @return List<CustomerDto>
     */

    List<CustomerDto> getCustomers(int offset, int limit);

    /**
     * @param email
     * @return
     */

    List<OrderDto> getActiveOrders(String email);
}
