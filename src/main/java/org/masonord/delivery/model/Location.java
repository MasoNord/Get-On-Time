package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.CountryType;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "country", nullable = false)
    private CountryType country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "zipCode", nullable = false)
    private String zipCode;

    @Column(name = "latitude", nullable = false)
    private float latitude;

    @Column(name = "longitude", nullable = false)
    private float longitude;

    @Column(name = "number", nullable = false)
    private int number; // No. of a home house, flat, apartment etc
}
