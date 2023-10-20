package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.model.Order;

import java.util.Set;

@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourierDto {
    private String email;

    private String firstName;

    private String lastName;

    private CourierType transport;

    // in format: first is latitude and next is longitude
    private float[] currentCoordinates;

    private String workingHours;

    private Set<Order> orders;
}
