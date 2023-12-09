package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.UserRoles;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {
    private String email;

    private String firstName;

    private String lastName;

    private UserRoles userRoles;

    private Set<OrderDto> orders;
}
