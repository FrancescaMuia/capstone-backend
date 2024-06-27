package it.epicode.viniEVinili.carts;

import it.epicode.viniEVinili.cartItems.CartItem;
import it.epicode.viniEVinili.cartItems.CartItemResponseDTO;
import it.epicode.viniEVinili.security.SecurityUserDetails;
import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public CartResponseDTO findById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));
        return mapCartToResponseDTO(cart);
    }

    public CartResponseDTO save(CartRequestDTO requestDTO) {
        Cart cart = mapRequestDTOToCart(requestDTO);
        Cart savedCart = cartRepository.save(cart);
        return mapCartToResponseDTO(savedCart);
    }

    public CartResponseDTO update(Long cartId, CartRequestDTO requestDTO) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));

        // Fetch user from repository based on userId in requestDTO
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUserId()));

        // Set the user in the existing cart
        existingCart.setUser(user);
        existingCart.setTotalAmount(calculateTotalAmount(existingCart));

        Cart updatedCart = cartRepository.save(existingCart);
        return mapCartToResponseDTO(updatedCart);
    }

    public void delete(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public List<CartResponseDTO> findAll() {
        log.info("findall avviata");
        List<Cart> carts = cartRepository.findAll();
        return carts.stream()
                .map(this::mapCartToResponseDTO)
                .collect(Collectors.toList());
    }

    private CartResponseDTO mapCartToResponseDTO(Cart cart) {
        CartResponseDTO responseDTO = new CartResponseDTO();
        responseDTO.setId(cart.getId());
        responseDTO.setUserId(cart.getUser().getId());
        responseDTO.setTotalAmount(cart.getTotalAmount());
        List<CartItemResponseDTO> cartItems = cart.getCartItems().stream()
                .map(this::mapCartItemToResponseDTO)
                .collect(Collectors.toList());
        responseDTO.setCartItems(cartItems);
        return responseDTO;
    }

    private CartItemResponseDTO mapCartItemToResponseDTO(CartItem cartItem) {
        CartItemResponseDTO responseDTO = new CartItemResponseDTO();
        responseDTO.setId(cartItem.getId());
        responseDTO.setCartId(cartItem.getCart().getId());
        responseDTO.setProductId(cartItem.getProduct().getId());
        responseDTO.setQuantity(cartItem.getQuantity());
        responseDTO.setPrice(cartItem.getPrice());  // Imposta il prezzo

        return responseDTO;
    }

    private Cart mapRequestDTOToCart(CartRequestDTO requestDTO) {
        Cart cart = new Cart();

        // Fetch user from repository based on userId in requestDTO
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUserId()));

        cart.setUser(user);
        return cart;
    }

    public double calculateTotalAmount(Cart cart) {
        log.info("calculateTotalAmount avviata");
        double tempt= cart.getCartItems().stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
        log.info("calc totale: " + tempt);
        return tempt;
    }

    // Metodo per ottenere l'ID dell'utente dal contesto di sicurezza
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        throw new IllegalStateException("Utente non autenticato");
    }
}
