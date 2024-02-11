package org.masonord.delivery.service.classes;
import org.masonord.delivery.dto.mapper.CompletedOrderMapper;
import org.masonord.delivery.dto.model.CompletedOrderDto;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.masonord.delivery.model.CompletedOrder;
import org.masonord.delivery.model.User;
import org.masonord.delivery.repository.CompletedOrderRepository;
import org.masonord.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CompletedOrderServiceImpl implements org.masonord.delivery.service.interfaces.CompletedOrderService {

    private final CompletedOrderRepository completedOrderRepository;
    private final UserRepository userRepository;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public CompletedOrderServiceImpl(CompletedOrderRepository completedOrderRepository, UserRepository userRepository, ExceptionHandler exceptionHandler) {
        this.completedOrderRepository = completedOrderRepository;
        this.userRepository = userRepository;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public List<CompletedOrderDto> getAllCompletedOrders() {
        List<CompletedOrderDto> completedOrdersDto = new ArrayList<>();
        List<CompletedOrder> completedOrders = completedOrderRepository.getCompletedOrders();
        for (CompletedOrder o : completedOrders) {
            completedOrdersDto.add(CompletedOrderMapper.toCompletedOrderDto(o));
        }
        return completedOrdersDto;
    }

    @Override
    public List<CompletedOrderDto> getCompletedOrdersByCourierEmail(String courierEmail) {
        User courier = userRepository.findUserByEmail(courierEmail);

        if (courier != null) {
            List<CompletedOrderDto> completedOrdersDto = new ArrayList<>();
            List<CompletedOrder> completedOrders = completedOrderRepository.getCompletedOrders();
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
        return exceptionHandler.throwException(entity, exception, args);
    }
}
