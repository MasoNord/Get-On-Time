package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.UserRoles;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("password")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDto {
    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String transport;

    private String workingHours;

    private String role;
}
