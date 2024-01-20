package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.OrderStatusType;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.order.OrderItem;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    private String id;

    // select any currency you want
    private float cost;

    private String courierEmail;

    private String customerEmail;

    private Set<DishDto> items;

    private OrderStatusType status;

    private String restaurantName;
}
