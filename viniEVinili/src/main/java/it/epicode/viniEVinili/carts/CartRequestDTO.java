package it.epicode.viniEVinili.carts;

import lombok.Data;

@Data
public class CartRequestDTO {

    private Long userId;
    private double totalAmount;

}
