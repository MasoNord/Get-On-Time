package org.masonord.delivery.annotations;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TransportValidator.class)
public @interface TransportValidation {

    public String message() default "Invalid a courier's transport type: must be walk bicycle or car";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
