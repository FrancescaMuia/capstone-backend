package it.epicode.viniEVinili.users;

import it.epicode.viniEVinili.addresses.AddressResponseDTO;
import it.epicode.viniEVinili.orders.OrderResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    //private AddressResponseDTO shippingAddress;
    //private AddressResponseDTO billingAddress;
    private List<AddressResponseDTO> addresses;
    private String phoneNumber;
    private String role;
    private List<OrderResponseDTO> orders;

}
