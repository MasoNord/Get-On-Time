package org.masonord.delivery.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.CourierType;
import org.masonord.delivery.enums.UserRoles;
import org.masonord.delivery.model.order.Order;
import org.masonord.delivery.model.restarurant.Restaurant;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 75)
    private String password;

    @Column(name = "role", nullable = false)
    private UserRoles role;

    @Column(name = "dc", nullable = false, length = 30)
    private String dc; // data of creation

    @Column(name = "du", nullable = false, length = 30)
    private String du; // data of updating

    @Column(name = "workingHours", length = 11)
    private String workingHours;

    @Column(name = "transport")
    private CourierType transport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToMany(mappedBy = "courier", fetch = FetchType.EAGER)
    private Set<CompletedOrder> completedOrders;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Order> orders;

    @OneToMany(mappedBy = "courier", fetch = FetchType.EAGER)
    private Set<Order> rides;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Restaurant> restaurants;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews;
}
