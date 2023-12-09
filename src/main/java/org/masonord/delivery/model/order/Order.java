package org.masonord.delivery.model.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.OrderStatusType;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.restarurant.Restaurant;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "weight", nullable = false)
    private float weight;

    @Column(name = "cost", nullable = false)
    private float cost;

    @Column(name = "deliveryHours", nullable = false)
    private String deliveryHours;

    @Column(name = "orderStatus", nullable = false)
    OrderStatusType orderStatusType;

    @Column(name = "dc", nullable = false, length = 30)
    private String dc; // data of creation

    @Column(name = "du", nullable = false, length = 30)
    private String du; // data of updating

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "courierId")
    private User courier;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customerId")
    private User customer;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "orderItemId")
    private OrderItem orderItems;
}
