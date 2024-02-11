package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CourierMetaInfoDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.dto.model.OrderDto;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.order.Order;

import java.text.ParseException;
import java.util.List;

public interface CourierService {
    /**
     * Sear an existing courier by his email;
     *
     * @param email
     * @return CourierDto
     */

    CourierDto findCourierByEmail(String email);

    /**
     * Search for all existing couriers
     *
     * @return List<CourierDto>
     *
     */

    List<CourierDto> getAllCouriers(int offset, int limit);

    /**
     * Assign a new order to a courier
     *
     * @param orderId
     * @param email
     * @return CourierDto
     */

    CourierDto acceptOrder(String orderId, String email);

    /**
     * Calculate rating and salary for a given courier
     *
     * @param courierEmail
     * @param order
     */

    void calculateRatingAndSalary(String courierEmail, Order order);

    List<OrderDto> getOrders(String email);

}
