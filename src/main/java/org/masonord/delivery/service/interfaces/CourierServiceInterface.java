package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.model.Courier;

import java.util.List;

public interface CourierServiceInterface {

    /**
     * Create a new courier
     *
     * @param courierDto
     * @return
     */
    CourierDto addNewCourier(CourierDto courierDto);

    /**
     * Search an existing courier by his id
     *
     * @param id
     * @return
     *
     */
    CourierDto findCourierById(Long id);

    /**
     * Sear an existing courier by his email;
     *
     * @param email
     * @return
     */

    CourierDto findCourierByEmail(String email);

    /**
     * Search for all existing couriers
     *
     * @return
     *
     */

    List<CourierDto> getAllCouriers();

    /**
     * Delete courier's record by id
     *
     * @param id
     *
     */
    void DeleteCourierById(Long id);

    /**
     * Update courier's profile
     *
     * @param newUserProfile
     * @return
     *
     */
    Courier updateProfile(String id, Courier newUserProfile);
}
