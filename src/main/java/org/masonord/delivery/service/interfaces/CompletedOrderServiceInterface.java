package org.masonord.delivery.service.interfaces;

import org.masonord.delivery.dto.model.CompletedOrderDto;

import java.util.List;

public interface CompletedOrderServiceInterface {
    /**
     * Get all completed orders for every courier in the db
     *
     * @return
     */
    public List<CompletedOrderDto> getAllCompletedOrders();

    /**
     * Get all completed orders for a courier by an email
     *
     * @param courierEmail
     * @return
     */

    public List<CompletedOrderDto> getCompletedOrdersByCourierEmail(String courierEmail);
}
