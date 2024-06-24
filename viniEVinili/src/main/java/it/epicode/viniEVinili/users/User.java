package it.epicode.viniEVinili.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.viniEVinili.addresses.Address;
import it.epicode.viniEVinili.orders.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String password;
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


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    private String phoneNumber;

    private String role; //MODIFICALO DOPO AVER IMPLEMENTATO LA SECURITY

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
