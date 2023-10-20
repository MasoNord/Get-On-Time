package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.CourierType;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "couriers")
public class Courier {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "transport", nullable = false)
    private CourierType transport;

    @Column(name = "currentCoordinates", nullable = false)
    private float[] currentCoordinates;

    @Column(name = "workingHours", nullable = false, length = 11)
    private String workingHours;

    @Column(name = "dc", nullable = false, length = 30)
    private String dc; // data of creation

    @Column(name = "du", nullable = false, length = 30)
    private String du; // data of updating

    @OneToMany(mappedBy = "courier")
    private Set<Order> orders;
}
