package it.epicode.viniEVinili.vinyls;

import it.epicode.viniEVinili.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/vinyl")
public class VinylController {

    @Autowired
    VinylService service;

    @GetMapping("/{id}")
    public ResponseEntity<VinylResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<VinylResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<VinylResponseDTO> create(@RequestBody VinylRequestDTO request){
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VinylResponseDTO> modify(@PathVariable Long id, @RequestBody VinylRequestDTO request){
        return ResponseEntity.ok(service.update(id, request));
    }

//    @PutMapping("/associate")
//    public ResponseEntity<Void> associateVinylAndWine(@RequestParam Long vinylId, @RequestParam Long wineId) {
//        service.associateVinylAndWine(vinylId, wineId);
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/associate")
    public ResponseEntity<Void> associateVinylAndWine(@RequestBody VinylWineAssociationRequest request) {
        service.associateVinylAndWine(request.getVinylId(), request.getWineId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VinylResponseDTO> patch(@PathVariable Long id, @RequestBody VinylRequestDTO request) {
        return ResponseEntity.ok(service.patch(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/productImg")
    public Vinyl uploadProductImg(@RequestParam("coverImg") MultipartFile file, @PathVariable Long id) {
        try {
            return service.saveProductImg(id, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
