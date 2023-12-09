package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.CustomerDto;
import org.masonord.delivery.dto.model.LocationDto;

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
     * @param offsetBasedPageRequest - contains limit and offset parameters to apply pagination
     * @return List<CustomerDto>
     */

    List<CustomerDto> getCustomers(OffsetBasedPageRequest offsetBasedPageRequest);

    /**
     * update a customer's record by the given email
     *
     * @param email
     * @param newCostumerProfile
     * @return CustomerDto
     */

    CustomerDto updateCustomer(String email, CustomerDto newCostumerProfile);

    /**
     * update a current location of a customer
     *
     * @param locationDto
     * @param email
     * @return
     */

    String updateCurrentLocation(LocationDto locationDto, String email);

    /**
     * delete a customer's record by the given id
     *
     * @param email
     */

    void deleteCustomer(String email);
}
