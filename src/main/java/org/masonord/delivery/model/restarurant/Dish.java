package org.masonord.delivery.model.restarurant;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.model.Review;
import org.masonord.delivery.model.order.OrderItem;
import org.masonord.delivery.model.restarurant.Menu;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "dishes")
public class Dish {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "price", nullable = false)
    private float cost;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "dish", fetch = FetchType.EAGER)
    private Set<Review> reviews;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "menuId")
    private Menu menu;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "orderItemId")
    private OrderItem orderItem;
}




