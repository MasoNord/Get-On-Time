package org.masonord.delivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.masonord.delivery.enums.CountryType;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "locationId", nullable = false, unique = true)
    private Long locationId;

    @Column(name = "country", nullable = false)
    private CountryType country;

    @Column(name = "street", nullable = false)
    private String street; // name of a street on which

    @Column(name = "zipCode", nullable = false)
    private String zipCode;

    @Column(name = "number", nullable = false)
    private int number; // No. of an apartment weather house, flat, etc
}
