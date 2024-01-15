package org.masonord.delivery.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuCreateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String type;


}
