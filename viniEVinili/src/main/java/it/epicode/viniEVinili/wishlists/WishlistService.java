package it.epicode.viniEVinili.wishlists;

import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.products.ProductRepository;
import it.epicode.viniEVinili.security.SecurityUserDetails;
import it.epicode.viniEVinili.tracks.Track;
import it.epicode.viniEVinili.tracks.TrackResponseDTO;
import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.users.UserRepository;
import it.epicode.viniEVinili.users.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    public WishlistResponseDTO findById(Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found with id: " + wishlistId));
        return mapWishlistToResponseDTO(wishlist);
    }

//    public WishlistResponseDTO save(WishlistRequestDTO requestDTO) {
//        Wishlist wishlist = mapRequestDTOToWishlist(requestDTO);
//        Wishlist savedWishlist = wishlistRepository.save(wishlist);
//        return mapWishlistToResponseDTO(savedWishlist);
//    }
public WishlistResponseDTO save(WishlistRequestDTO requestDTO) {
    Long userId = userService.getCurrentUserId(); // Ottieni l'ID utente
    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));

    Wishlist wishlist = mapRequestDTOToWishlist(requestDTO, user);
    wishlist.setUser(user); // Imposta l'utente nella wishlist
    Wishlist savedWishlist = wishlistRepository.save(wishlist);
    return mapWishlistToResponseDTO(savedWishlist);
}




    public WishlistResponseDTO update(Long wishlistId, WishlistRequestDTO requestDTO) {
        Wishlist existingWishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found with id: " + wishlistId));

        // Mappa gli ID dei prodotti a oggetti Product
        List<Product> products = requestDTO.getProductIds().stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id)))
                .collect(Collectors.toList());

        // Imposta la lista di prodotti aggiornata sulla wishlist esistente
        existingWishlist.setProducts(products);

        // Salva la wishlist aggiornata nel repository
        Wishlist updatedWishlist = wishlistRepository.save(existingWishlist);

        // Ritorna il DTO di risposta mappato dalla wishlist aggiornata
        return mapWishlistToResponseDTO(updatedWishlist);
    }

    public void delete(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    public List<WishlistResponseDTO> findAll() {
        List<Wishlist> wishlists = wishlistRepository.findAll();
        return wishlists.stream()
                .map(this::mapWishlistToResponseDTO)
                .collect(Collectors.toList());
    }

    private WishlistResponseDTO mapWishlistToResponseDTO(Wishlist wishlist) {
        WishlistResponseDTO responseDTO = new WishlistResponseDTO();
        responseDTO.setId(wishlist.getId());
        responseDTO.setUser(wishlist.getUser());
        responseDTO.setProducts(wishlist.getProducts());
        return responseDTO;
    }

//    private Wishlist mapRequestDTOToWishlist(WishlistRequestDTO requestDTO) {
//        Wishlist wishlist = new Wishlist();
//        //wishlist.setUser(requestDTO.getUser());
//
//        List<Product> products = requestDTO.getProductIds().stream()
//                .map(id -> productRepository.findById(id)
//                        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id)))
//                .collect(Collectors.toList());
//
//        wishlist.setProducts(products);
//
//        return wishlist;
//    }
private Wishlist mapRequestDTOToWishlist(WishlistRequestDTO requestDTO, User user) {
    Wishlist wishlist = new Wishlist();
    List<Product> products = productRepository.findAllById(requestDTO.getProductIds());
    if (products.size() != requestDTO.getProductIds().size()) {
        throw new RuntimeException("Uno o più prodotti non sono stati trovati");
    }
    wishlist.setUser(user);
    wishlist.setProducts(products);
    return wishlist;
}






//    // Metodo per ottenere l'ID dell'utente dal contesto di sicurezza
//    private Long getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
//            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
//            return userDetails.getUserId();
//        }
//        throw new IllegalStateException("Utente non autenticato");
//    }



}
