package org.masonord.delivery.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.dto.model.MenuDto;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.dish.Dish;

import java.util.Set;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantCreateRequest {
    @NotEmpty
    private String name;

    @NotNull
    private LocationAddRequest location;
}
