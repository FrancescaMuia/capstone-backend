package it.epicode.viniEVinili.carts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> findById(@PathVariable Long id) {
        CartResponseDTO response = cartService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDTO>> findAll() {
        List<CartResponseDTO> response = cartService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> create(@RequestBody CartRequestDTO request) {
        CartResponseDTO response = cartService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponseDTO> update(@PathVariable Long id, @RequestBody CartRequestDTO request) {
        CartResponseDTO response = cartService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();  // Status 204 for successful deletion
    }

    @PostMapping("/purchase/{id}")
    public ResponseEntity<Void> purchase(@PathVariable Long id) {
        // Gestire il processo di acquisto...

        // Svuotare il carrello dopo l'acquisto
        cartService.clearCart(id);

        return ResponseEntity.ok().build();
    }
}
