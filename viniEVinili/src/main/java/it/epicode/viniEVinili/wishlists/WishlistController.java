package it.epicode.viniEVinili.wishlists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponseDTO> getWishlistById(@PathVariable Long id) {
        WishlistResponseDTO wishlist = wishlistService.findById(id);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponseDTO>> getAllWishlists() {
        List<WishlistResponseDTO> wishlists = wishlistService.findAll();
        return ResponseEntity.ok(wishlists);
    }

    @PostMapping
    public ResponseEntity<WishlistResponseDTO> createWishlist(@RequestBody WishlistRequestDTO requestDTO) {
        WishlistResponseDTO newWishlist = wishlistService.save(requestDTO);
        return ResponseEntity.ok(newWishlist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponseDTO> updateWishlist(@PathVariable Long id, @RequestBody WishlistRequestDTO requestDTO) {
        WishlistResponseDTO updatedWishlist = wishlistService.update(id, requestDTO);
        return ResponseEntity.ok(updatedWishlist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long id) {
        wishlistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
