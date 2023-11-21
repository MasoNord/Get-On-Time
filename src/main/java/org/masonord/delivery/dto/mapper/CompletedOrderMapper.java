package org.masonord.delivery.dto.mapper;

import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.model.CompletedOrder;

public class CompletedOrderMapper {
    public static CompletedOrderDto toCompletedOrderDto(CompletedOrder completedOrder) {
        return new CompletedOrderDto()
                .setOrderId(completedOrder.getOrderId())
                .setCourierEmail(completedOrder.getCourier().getEmail())
                .setCompletedTime(completedOrder.getCompletedTime());
    }
}
