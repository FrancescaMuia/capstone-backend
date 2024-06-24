package it.epicode.viniEVinili.cartItems;

import it.epicode.viniEVinili.tracks.TrackRequestDTO;
import it.epicode.viniEVinili.tracks.TrackResponseDTO;
import it.epicode.viniEVinili.tracks.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartitem")
public class CartItemController {

    @Autowired
    CartItemService service;

    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<CartItemResponseDTO> create(@RequestBody CartItemRequestDTO request){
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponseDTO> modify(@PathVariable Long id, @RequestBody CartItemRequestDTO request){
        return ResponseEntity.ok(service.update(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
