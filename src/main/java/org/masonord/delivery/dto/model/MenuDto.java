package org.masonord.delivery.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.model.restarurant.Dish;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MenuDto {
    private String name;

    private String description;

    private String menuType;

    private String restaurantName;

    private Set<DishDto> dishes;

    private Set<ReviewDto> review;
}
