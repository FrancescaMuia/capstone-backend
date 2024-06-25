package it.epicode.viniEVinili.wishlists;

import it.epicode.viniEVinili.users.User;
import lombok.Data;
import org.springframework.core.SpringVersion;

import java.util.List;

@Data
public class WishlistRequestDTO {

//    private Long userId; //non mi serve l'id dello user perché lo prendo da jwt
    private List<Long> productIds;
}
