package it.epicode.viniEVinili.carts;

import lombok.Data;

@Data
public class CartResponseDTO {

    private Long id;
    private Long userId;
    private double totalAmount;
}
