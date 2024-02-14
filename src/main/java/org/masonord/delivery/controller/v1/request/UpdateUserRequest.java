package org.masonord.delivery.controller.v1.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.masonord.delivery.enums.CourierType;

@Data
public class UpdateUserRequest {

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 26)
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 26)
    private String lastName;

    @Size(min = 3, max = 52)
    private String email;

    private CourierType courierType;
}
