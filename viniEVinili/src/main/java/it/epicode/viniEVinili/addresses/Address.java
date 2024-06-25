package it.epicode.viniEVinili.addresses;

import it.epicode.viniEVinili.users.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String city;
    private String toponym;
    private String name;
    private String streetNumber;
    private String zipCode;

    /*@Column(nullable = true)
    private boolean isShipping;
    @Column(nullable = true)
    private boolean isBilling;

     */


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
