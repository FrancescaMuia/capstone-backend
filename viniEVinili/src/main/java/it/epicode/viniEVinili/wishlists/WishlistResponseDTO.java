package it.epicode.viniEVinili.wishlists;

import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.users.User;
import lombok.Data;

import java.util.List;

@Data
public class WishlistResponseDTO {

    private Long id;
    private User user;
    private List<Product> products;
}
