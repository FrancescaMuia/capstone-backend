package it.epicode.viniEVinili.cartItems;

import lombok.Data;

@Data
public class CartItemResponseDTO {

    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;
}
