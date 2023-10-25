package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.model.Courier;
import org.masonord.delivery.model.Customer;
import org.masonord.delivery.model.Location;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {
    // take any measurement metrics, pounds or kgs
    // I'll stay with kg, because I've gotten used to it
    private float weight;

    // select any currency you want
    private float cost;

    private String deliveryHours;

    private Courier courier;

    private Customer customer;

    private Location location;
}
