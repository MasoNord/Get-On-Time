package org.masonord.delivery.util;

import net.datafaker.Faker;
import net.datafaker.providers.base.Options;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.UserRoles;
import org.springframework.stereotype.Component;

@Component
public class FakeDataUtil {

    private final static Faker faker = new Faker();
    private final static Options opt = faker.options();

    public String generateFirstName() {
        return faker.name().firstName();
    }

    public String generateLastName() {
        return faker.name().lastName();
    }

    public String generateEmail() {
        return faker.internet().emailAddress();
    }

    public String generatePassword() {
        return faker.internet().password();
    }

    public UserRoles generateRole() {
        return opt.option(UserRoles.class);
    }

    public CourierType generateCourier() {
        return opt.option(CourierType.class);
    }


}
