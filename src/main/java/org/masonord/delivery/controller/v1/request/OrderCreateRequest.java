package org.masonord.delivery.controller.v1.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderCreateRequest {

    @NotNull
    private float weight;

    @NotNull
    private float cost;

    @NotEmpty
    private String deliveryHours;

    @NotEmpty
    private String customerEmail;

    @NotNull
    private LocationAddRequest location;


}
