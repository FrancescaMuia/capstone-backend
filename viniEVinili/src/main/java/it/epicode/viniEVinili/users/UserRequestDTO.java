package it.epicode.viniEVinili.users;


import it.epicode.viniEVinili.security.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class UserRequestDTO {



    @NotBlank(message = "Il tuo nome non può essere vuoto")
    private String name;

    @NotBlank(message = "Il tuo cognome non può essere vuoto")
    private String surname;

    @NotBlank(message = "Lo username non può contenere solo spazi vuoti")
    @Size(max = 50, message = "Il tuo username è troppo lungo, max 50 caratteri")
    private String username;

    @Email(message = "Inserisci una email valida")
    private String email;

    @NotBlank(message = "La password non può contenere solo spazi vuoti")
    @Size(max = 125, message = "La password è troppo lunga, max 125 caratteri")
    private String password;

    private String city;
    private String toponym;
    private String addressName;
    private String streetNumber;
    private String zipCode;
    private String phoneNumber;
    private List<Roles> roles;

    @Builder(setterPrefix = "with")
    public UserRequestDTO(String name, String surname, String username, String email, String password,
                          String city, String toponym, String addressName, String streetNumber, String zipCode,
                          String phoneNumber) {

        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
        this.toponym = toponym;
        this.addressName = addressName;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
    }


}

