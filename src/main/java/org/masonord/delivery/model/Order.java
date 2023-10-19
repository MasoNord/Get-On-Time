package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Order")
public class Order {
    @Id
    @Column(name = "orderId", nullable = false, unique = true)
    private String orderId;

    @Column(name = "weight", nullable = false)
    private float weight;

    @Column(name = "cost", nullable = false)
    private float cost;

    @Column(name = "deliveryHours", nullable = false)
    private String deliveryHours;

    @ManyToOne
    @JoinColumn(name = "courierId", nullable = false)
    private Courier courier;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "locationId")
//    private Location location;
}
