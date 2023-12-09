package org.masonord.delivery.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class TransportValidator implements ConstraintValidator<TransportValidation, String> {
    @Override
    public boolean isValid(String transportName, ConstraintValidatorContext constraintValidatorContext) {
        List list = Arrays.asList(new String[] {"WALK", "CAR", "BICYCLE"});
        return list.contains(transportName);
    }
}
