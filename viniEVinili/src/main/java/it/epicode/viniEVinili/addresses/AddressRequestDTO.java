package it.epicode.viniEVinili.addresses;

import lombok.Data;

@Data
public class AddressRequestDTO {

    private String city;

    private String toponym;

    private String name;

    private String streetNumber;

    private String zipCode;

    /*private boolean isShipping;
    private boolean isBilling;

     */
}
