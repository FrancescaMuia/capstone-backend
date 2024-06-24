package it.epicode.viniEVinili.cartItems;

import it.epicode.viniEVinili.carts.Cart;
import it.epicode.viniEVinili.carts.CartRepository;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.products.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItemResponseDTO findById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
        return mapCartItemToResponseDTO(cartItem);
    }

    public CartItemResponseDTO save(CartItemRequestDTO requestDTO) {
        CartItem cartItem = mapRequestDTOToCartItem(requestDTO);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return mapCartItemToResponseDTO(savedCartItem);
    }

    public CartItemResponseDTO update(Long cartItemId, CartItemRequestDTO requestDTO) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));

        // Set cart and product based on IDs from requestDTO
        existingCartItem.setCart(fetchCartById(requestDTO.getCartId()));
        existingCartItem.setProduct(fetchProductById(requestDTO.getProductId()));
        existingCartItem.setQuantity(requestDTO.getQuantity());

        CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
        return mapCartItemToResponseDTO(updatedCartItem);
    }

    public void delete(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public List<CartItemResponseDTO> findAll() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        return cartItems.stream()
                .map(this::mapCartItemToResponseDTO)
                .collect(Collectors.toList());
    }

    private CartItemResponseDTO mapCartItemToResponseDTO(CartItem cartItem) {
        CartItemResponseDTO responseDTO = new CartItemResponseDTO();
        responseDTO.setId(cartItem.getId());
        responseDTO.setCartId(cartItem.getCart().getId());
        responseDTO.setProductId(cartItem.getProduct().getId());
        responseDTO.setQuantity(cartItem.getQuantity());
        return responseDTO;
    }

    private CartItem mapRequestDTOToCartItem(CartItemRequestDTO requestDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(fetchCartById(requestDTO.getCartId()));
        cartItem.setProduct(fetchProductById(requestDTO.getProductId()));
        cartItem.setQuantity(requestDTO.getQuantity());
        return cartItem;
    }

    private Cart fetchCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));
    }

    private Product fetchProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }
}
