package org.masonord.delivery.model.restarurant;

import jakarta.persistence.*;
import org.masonord.delivery.enums.MenuType;
import org.masonord.delivery.model.Review;
import org.masonord.delivery.model.restarurant.dish.Dish;

import java.util.Set;

@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    private String id;

    @Column(name = "dc", nullable = false, length = 30)
    private String dc; // data of creation

    @Column(name = "du", nullable = false, length = 30)
    private String du; // data of updating

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "menuType")
    private MenuType menuType;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}
