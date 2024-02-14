package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDto {
    public String name;

    public Set<OrderDto> orders;

    public Set<MenuDto> menus;

    public Set<ReviewDto> reviews;

    public LocationDto location;
}
