package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "completedOrders")
public class CompletedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "userId")
    private User courier;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "cost")
    private float cost;

    @Column(name = "completedTime", nullable = false, unique = true,  length = 30)
    private String completedTime;
}
