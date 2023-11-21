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

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "courierId")
    private Courier courier;

    private String orderId;

    @Column(name = "completedTime", nullable = false, unique = true,  length = 30)
    private String completedTime;
}
