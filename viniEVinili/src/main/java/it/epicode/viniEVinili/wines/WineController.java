package it.epicode.viniEVinili.wines;


import it.epicode.viniEVinili.vinyls.Vinyl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/wine")
public class WineController {

    @Autowired
    WineService service;

    @GetMapping("/{id}")
    public ResponseEntity<WineResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<WineResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<WineResponseDTO> create(@RequestBody WineRequestDTO request){
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WineResponseDTO> modify(@PathVariable Long id, @RequestBody WineRequestDTO request){
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WineResponseDTO> patch(@PathVariable Long id, @RequestBody WineRequestDTO request) {
        return ResponseEntity.ok(service.patch(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/productImg")
    public Wine uploadProductImg(@RequestParam("coverImg") MultipartFile file, @PathVariable Long id) {
        try {
            return service.saveProductImg(id, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
