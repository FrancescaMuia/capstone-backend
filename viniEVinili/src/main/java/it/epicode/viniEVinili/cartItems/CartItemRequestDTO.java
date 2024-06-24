package it.epicode.viniEVinili.cartItems;

import lombok.Data;

@Data
public class CartItemRequestDTO {

    private Long cartId;
    private Long productId;
    private int quantity;
}
