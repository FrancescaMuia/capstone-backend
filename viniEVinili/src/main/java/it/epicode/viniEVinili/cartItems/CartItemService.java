package it.epicode.viniEVinili.cartItems;

import it.epicode.viniEVinili.carts.Cart;
import it.epicode.viniEVinili.carts.CartRepository;
import it.epicode.viniEVinili.carts.CartResponseDTO;
import it.epicode.viniEVinili.carts.CartService;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.products.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemService {

    private static final Logger log = LoggerFactory.getLogger(CartItemService.class);
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    public CartItemResponseDTO findById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
        return mapCartItemToResponseDTO(cartItem);
    }

    public CartItemResponseDTO save(CartItemRequestDTO requestDTO) {
        CartItem cartItem = mapRequestDTOToCartItem(requestDTO);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        updateCartTotalAmount(savedCartItem.getCart());
        return mapCartItemToResponseDTO(savedCartItem);
    }

    public CartItemResponseDTO update(Long cartItemId, CartItemRequestDTO requestDTO) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));

        // Update quantity
        existingCartItem.setQuantity(requestDTO.getQuantity());

        // Recalculate total price
        double totalPrice = existingCartItem.getProduct().getPrice() * requestDTO.getQuantity();
        existingCartItem.setPrice(totalPrice);

        // Save updated CartItem
        CartItem updatedCartItem = cartItemRepository.save(existingCartItem);
        log.info("effettuato  CartItem updatedCartItem = cartItemRepository.save(existingCartItem);");
        log.info("sto per eseguire updateCartTotalAmount(updatedCartItem.getCart());");
        updateCartTotalAmount(updatedCartItem.getCart());
        log.info("ho eseguito updateCartTotalAmount(updatedCartItem.getCart()); sto per effettuare return");
        return mapCartItemToResponseDTO(updatedCartItem);
    }


    public void delete(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
        cartItemRepository.delete(cartItem);

        // Aggiorna il totale del carrello
        updateCartTotalAmount(cartItem.getCart());
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
        responseDTO.setPrice(cartItem.getPrice());  // Ensure price is set in the response
        Product product = cartItem.getProduct();
            if (product != null) {
                responseDTO.setProductName(product.getName());
            }
        return responseDTO;
    }

    private CartItem mapRequestDTOToCartItem(CartItemRequestDTO requestDTO) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(fetchCartById(requestDTO.getCartId()));
        cartItem.setProduct(fetchProductById(requestDTO.getProductId()));
        cartItem.setQuantity(requestDTO.getQuantity());
        // Initialize price based on product price and quantity
        double price = cartItem.getProduct().getPrice() * requestDTO.getQuantity();
        cartItem.setPrice(price);
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

    public void updateCartTotalAmount(Cart cart) {
        double totalAmount = cartService.calculateTotalAmount(cart);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
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
}
