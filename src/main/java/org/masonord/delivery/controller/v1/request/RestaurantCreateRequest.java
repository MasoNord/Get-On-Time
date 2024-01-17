package org.masonord.delivery.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    @NotNull
    private Set<MenuCreateRequest> menus;
}
