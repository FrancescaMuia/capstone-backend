package it.epicode.viniEVinili.carts;

import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        Cart updatedCart = cartRepository.save(existingCart);
        return mapCartToResponseDTO(updatedCart);
    }

    public void delete(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public List<CartResponseDTO> findAll() {
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
}
