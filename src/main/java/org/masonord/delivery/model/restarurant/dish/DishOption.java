package org.masonord.delivery.model.restarurant.dish;

import jakarta.persistence.*;

@Entity
@Table(name = "dishOptions")
public class DishOption {
    @Id
    @Column(name = "id", nullable = false, unique = true, length = 32 )
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "extra", nullable = false)
    private int extra;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "dishId")
    private Dish dish;
}
