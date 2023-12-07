package org.masonord.delivery.model;

import jakarta.persistence.*;
import org.masonord.delivery.model.restarurant.Menu;
import org.masonord.delivery.model.restarurant.Restaurant;
import org.masonord.delivery.model.restarurant.dish.Dish;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "dc", nullable = false, length = 30)
    private String dc; // data of creation

    @Column(name = "du", nullable = false, length = 30)
    private String du; // data of updating

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "star", length = 5)
    private int star;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "dishId")
    private Dish dish;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "manuId")
    private Menu menu;
}
