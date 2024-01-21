package org.masonord.delivery.service.classes;
import org.masonord.delivery.dto.mapper.CompletedOrderMapper;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.CompletedOrderRep;
import org.masonord.delivery.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("completedOrderService")
public class CompletedOrderServiceImpl implements org.masonord.delivery.service.interfaces.CompletedOrderService {

    @Autowired
    CompletedOrderRep completedOrderRep;

    @Autowired
    UserRep userRep;

    @Override
    public List<CompletedOrderDto> getAllCompletedOrders() {
        List<CompletedOrderDto> completedOrdersDto = new ArrayList<>();
        List<CompletedOrder> completedOrders = completedOrderRep.getCompletedOrders();
        for (CompletedOrder o : completedOrders) {
            completedOrdersDto.add(CompletedOrderMapper.toCompletedOrderDto(o));
        }
        return completedOrdersDto;
    }

    @Override
    public List<CompletedOrderDto> getCompletedOrdersByCourierEmail(String courierEmail) {
        User courier = userRep.findUserByEmail(courierEmail);

        if (courier != null) {
            List<CompletedOrderDto> completedOrdersDto = new ArrayList<>();
            List<CompletedOrder> completedOrders = completedOrderRep.getCompletedOrders();
            for (CompletedOrder o : completedOrders) {
                if (Objects.equals(o.getCourier().getEmail(), courierEmail)) {
                    completedOrdersDto.add(CompletedOrderMapper.toCompletedOrderDto(o));
                }
            }
            return completedOrdersDto;
        }

        throw exception(ModelType.COURIER, ExceptionType.ENTITY_NOT_FOUND, courierEmail);
    }

    private RuntimeException exception(ModelType entity, ExceptionType exception, String ...args) {
        return ExceptionHandler.throwException(entity, exception, args);
    }
}
