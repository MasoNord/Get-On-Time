package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.model.Courier;

import java.util.List;

public interface CourierRepository {

    Courier addCourier(CourierDto courierDto);

    Courier getById(String id);

    List<Courier> getAll();

    void removeCourier();
}
