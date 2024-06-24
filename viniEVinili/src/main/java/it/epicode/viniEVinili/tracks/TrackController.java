package it.epicode.viniEVinili.tracks;

import it.epicode.viniEVinili.artists.ArtistRequestDTO;
import it.epicode.viniEVinili.artists.ArtistResponseDTO;
import it.epicode.viniEVinili.artists.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/track")
public class TrackController {

    @Autowired
    TrackService service;

    @GetMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrackResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<TrackResponseDTO> create(@RequestBody TrackRequestDTO request){
        return ResponseEntity.ok(service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackResponseDTO> modify(@PathVariable Long id, @RequestBody TrackRequestDTO request){
        return ResponseEntity.ok(service.update(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
