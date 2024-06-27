package it.epicode.viniEVinili.carts;

import it.epicode.viniEVinili.cartItems.CartItemResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {

    private Long id;
    private Long userId;
    private double totalAmount;
    private List<CartItemResponseDTO> cartItems;
}
