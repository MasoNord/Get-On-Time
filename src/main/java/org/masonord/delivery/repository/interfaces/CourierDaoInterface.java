package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.model.Courier;

import java.util.List;

public interface CourierDaoInterface {

    Courier createCourier(Courier courier);

    Courier getCourier(String id);

    List<Courier> getCouriers();

    void deleteCourier(String id);
}
