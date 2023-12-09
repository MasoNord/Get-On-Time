package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.controller.v1.request.OffsetBasedPageRequest;
import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.CourierMetaInfoDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.order.Order;

import java.text.ParseException;
import java.util.List;

public interface CourierService {
    /**
     * Sear an existing courier by his email;
     *
     * @param email
     * @return CoureirDto
     */

    CourierDto findCourierByEmail(String email);

    /**
     * Search for all existing couriers
     *
     * @return List<CourierDto>
     *
     */

    List<CourierDto> getAllCouriers(OffsetBasedPageRequest offsetBasedPageRequest);

    /**
     * Update courier's profile
     *
     * @param newUserProfile
     * @return User
     *
     */
    User updateProfile(String id, User newUserProfile);

    /**
     * Update courier's current location and return a corresponding message
     *
     * @param locationDto
     * @return String
     */
    String updateCurrentLocation(LocationDto locationDto, String email);

    /**
     * Assign a new order to a courier
     *
     * @param orderId
     * @param email
     * @return CourierDto
     */

    CourierDto setNewOrder(String orderId, String email);

    /**
     * Calculate rating and salary for a given courier
     *
     * @param courierEmail
     * @param order
     */

    void calculateRatingAndSalary(String courierEmail, Order order);

    /**
     * Get meta info for a given courier
     * This displays the following information:
     * Courier's earnings and Courier's rating, among others
     *
     * @param courierEmail
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */

    CourierMetaInfoDto getMetaInfo(String courierEmail, String startDate, String endDate) throws ParseException;

}
