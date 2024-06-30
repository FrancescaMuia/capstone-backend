package it.epicode.viniEVinili.carts;

import it.epicode.viniEVinili.cartItems.CartItem;
import it.epicode.viniEVinili.cartItems.CartItemRepository;
import it.epicode.viniEVinili.cartItems.CartItemResponseDTO;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.products.ProductRepository;
import it.epicode.viniEVinili.security.SecurityUserDetails;
import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.users.UserRepository;
import it.epicode.viniEVinili.users.UserService;
import it.epicode.viniEVinili.wishlists.Wishlist;
import it.epicode.viniEVinili.wishlists.WishlistRequestDTO;
import it.epicode.viniEVinili.wishlists.WishlistResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartResponseDTO findById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));
        return mapCartToResponseDTO(cart);
    }

//    public CartResponseDTO save(CartRequestDTO requestDTO) {
//        Cart cart = mapRequestDTOToCart(requestDTO);
//        Cart savedCart = cartRepository.save(cart);
//        return mapCartToResponseDTO(savedCart);
//    }

    public CartResponseDTO save(CartRequestDTO requestDTO) {
        Long userId = userService.getCurrentUserId(); // Ottieni l'ID utente dal JWT
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalAmount(requestDTO.getTotalAmount());

        Cart savedCart = cartRepository.save(cart);
        return mapCartToResponseDTO(savedCart);
    }


//    public CartResponseDTO update(Long cartId, CartRequestDTO requestDTO) {
//        Cart existingCart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));
//
//        // Fetch user from repository based on userId in requestDTO
//        User user = userRepository.findById(requestDTO.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUserId()));
//
//        // Set the user in the existing cart
//        existingCart.setUser(user);
//        existingCart.setTotalAmount(calculateTotalAmount(existingCart));
//
//        Cart updatedCart = cartRepository.save(existingCart);
//        return mapCartToResponseDTO(updatedCart);
//    }

    public CartResponseDTO update(Long cartId, CartRequestDTO requestDTO) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));

        // Non serve più fetchare l'utente dal repository, perché l'ID utente è nel contesto di sicurezza
        Long userId = userService.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        existingCart.setUser(user);
        existingCart.setTotalAmount(requestDTO.getTotalAmount());

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

//    private Cart mapRequestDTOToCart(CartRequestDTO requestDTO) {
//        Cart cart = new Cart();
//
//        // Fetch user from repository based on userId in requestDTO
//        User user = userRepository.findById(requestDTO.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUserId()));
//
//        cart.setUser(user);
//        return cart;
//    }

    public double calculateTotalAmount(Cart cart) {
        log.info("calculateTotalAmount avviata");
        double tempt= cart.getCartItems().stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
        log.info("calc totale: " + tempt);
        return tempt;
    }


    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));

        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            cartItemRepository.delete(cartItem);
        }

        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }
}
