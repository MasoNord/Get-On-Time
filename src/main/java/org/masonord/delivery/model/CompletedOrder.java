package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "completedOrders")
public class CompletedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "courierId", nullable = false, unique = true)
    private String courierId;

    @Column(name = "orderId", nullable = false, unique = true)
    private String orderId;

    @Column(name = "completedTime", nullable = false, unique = true,  length = 30)
    private String completedTime;
}
