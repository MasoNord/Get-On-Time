package org.masonord.delivery.model.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.model.restarurant.dish.Dish;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderItems")
public class OrderItem {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    private String id;

    @OneToMany(mappedBy = "orderItem", fetch = FetchType.EAGER)
    private Set<Dish> dishes;
}
