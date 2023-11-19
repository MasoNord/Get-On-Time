package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.model.Order;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierDto {
    private String email;

    private String firstName;

    private String lastName;

    private CourierType transport;

    private String workingHours;

    private Set<OrderDto> orders;
}
