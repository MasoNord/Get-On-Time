package org.masonord.delivery.model.restarurant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.model.Review;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "dc", nullable = false, length = 30)
    private String dc; // data of creation

    @Column(name = "du", nullable = false, length = 30)
    private String du; // data of updating

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "menuType")
    private String menuType;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}
