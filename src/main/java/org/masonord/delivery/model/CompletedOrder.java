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
@Entity(name = "CompletedOrder")
public class CompletedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "completedOrderId", nullable = false, unique = true)
    private Long completedOrderId;

    @Column(name = "courierId", nullable = false, unique = true)
    private String courierId;

    @Column(name = "orderId", nullable = false, unique = true)
    private String orderId;

    @Column(name = "completedTime", nullable = false, unique = true,  length = 30)
    private String completedTime;
}
