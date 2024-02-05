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
public class LocationAddRequest {

    @NotNull
    private String country;

    @NotEmpty
    private String street;

    @NotEmpty
    private String city;

    @NotNull
    private int number;

    @NotEmpty
    private String zip;
}
