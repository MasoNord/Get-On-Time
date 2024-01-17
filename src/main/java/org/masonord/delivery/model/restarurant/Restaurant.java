package org.masonord.delivery.model.restarurant;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.model.Location;
import org.masonord.delivery.model.Review;
import org.masonord.delivery.model.User;
import org.masonord.delivery.model.order.Order;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Menu> menus;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Order> orders;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "locationId")
    private Location location;

    @ManyToOne(cascade =  CascadeType.DETACH)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
