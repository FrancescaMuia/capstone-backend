package it.epicode.viniEVinili.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.epicode.viniEVinili.addresses.Address;
import it.epicode.viniEVinili.orders.Order;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.security.Roles;
import it.epicode.viniEVinili.wishlists.Wishlist;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;
    private String email;
    private String name;
    private String surname;

    /*@JsonIgnore
    @OneToMany
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

     */


    /*@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

     */
    private String city;
    private String toponym;
    private String addressName;
    private String streetNumber;
    private String zipCode;

    private String phoneNumber;

    @Column(length = 125, nullable = false)
    private String password;
    private String avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    private final List<Roles> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Wishlist wishlist;
}
