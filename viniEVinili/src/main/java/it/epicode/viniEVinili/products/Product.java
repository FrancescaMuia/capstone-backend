package it.epicode.viniEVinili.products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.wishlists.Wishlist;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private double price;
    private boolean available;
    private int year;

    @ManyToMany(mappedBy = "products")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private List<Wishlist> wishlists;
}
