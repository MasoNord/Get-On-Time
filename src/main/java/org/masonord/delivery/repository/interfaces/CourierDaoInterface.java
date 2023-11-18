package org.masonord.delivery.repository.interfaces;

import org.masonord.delivery.dto.model.CourierDto;
import org.masonord.delivery.dto.model.LocationDto;
import org.masonord.delivery.model.Courier;

import java.util.List;

public interface CourierDaoInterface {

    Courier createCourier(Courier courier);

    Courier getCourierById(Long id);

    Courier getCourierByEmail(String email);

    List<Courier> getCouriers();

    List<Courier> getCouriers(int offset, int limit);

    void deleteCourier(Long id);

    Courier updateProfile(Courier courier);

}
