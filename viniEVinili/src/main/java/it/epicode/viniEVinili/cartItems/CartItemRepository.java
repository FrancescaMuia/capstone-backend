package it.epicode.viniEVinili.cartItems;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository <CartItem, Long> {
}
