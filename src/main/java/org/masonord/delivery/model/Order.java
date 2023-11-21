package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "courierId")
    private Courier courier;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "locationId")
    private Location location;
}
