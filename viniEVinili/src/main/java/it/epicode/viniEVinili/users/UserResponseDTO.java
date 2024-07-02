package it.epicode.viniEVinili.users;


import it.epicode.viniEVinili.orders.OrderResponseDTO;
import it.epicode.viniEVinili.security.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String city;
    private String toponym;
    private String addressName;
    private String streetNumber;
    private String zipCode;
    private String phoneNumber;
    private String avatar;
    private List<Roles> roles;
    private List<OrderResponseDTO> orders;

}
