package it.epicode.viniEVinili.artists;

import it.epicode.viniEVinili.vinyls.VinylRequestDTO;
import it.epicode.viniEVinili.vinyls.VinylResponseDTO;
import it.epicode.viniEVinili.vinyls.VinylService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    @Autowired
    ArtistService service;

    @GetMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ArtistResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<ArtistResponseDTO> create(@RequestBody ArtistRequestDTO request){
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistResponseDTO> modify(@PathVariable Long id, @RequestBody ArtistRequestDTO request){
        return ResponseEntity.ok(service.update(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
