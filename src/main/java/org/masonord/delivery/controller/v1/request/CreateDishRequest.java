package org.masonord.delivery.controller.v1.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.masonord.delivery.model.order.OrderItem;

public class CreateDishRequest {

    @NotEmpty
    private String name;

    @NotNull
    private float cost;

    @NotEmpty
    private String description;

    @NotEmpty
    private OrderItem orderItem;

    private String menu;

    private String restaurantName;

    @NotNull
    private double lat;

    @NotNull
    private double lon;
}
