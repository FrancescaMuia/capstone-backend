package it.epicode.viniEVinili.users;

import it.epicode.viniEVinili.addresses.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestDTO {

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    //private Address shippingAddress;
    //private Address billingAddress;
    //private String phoneNumber;
    private String role;
}
