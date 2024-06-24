package it.epicode.viniEVinili.artists;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    public ArtistResponseDTO findById(Long artistId) {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        return mapArtistToResponseDTO(artist);
    }

    public ArtistResponseDTO save(ArtistRequestDTO requestDTO) {
        Artist artist = mapRequestDTOToArtist(requestDTO);
        Artist savedArtist = artistRepository.save(artist);
        return mapArtistToResponseDTO(savedArtist);
    }

    public ArtistResponseDTO update(Long artistId, ArtistRequestDTO requestDTO) {
        Artist existingArtist = artistRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + artistId));
        existingArtist.setName(requestDTO.getName());

        Artist updatedArtist = artistRepository.save(existingArtist);
        return mapArtistToResponseDTO(updatedArtist);
    }

    public void delete(Long artistId) {
        artistRepository.deleteById(artistId);
    }

    public List<ArtistResponseDTO> findAll() {
        List<Artist> artists = artistRepository.findAll();
        return artists.stream()
                .map(this::mapArtistToResponseDTO)
                .collect(Collectors.toList());
    }

    private ArtistResponseDTO mapArtistToResponseDTO(Artist artist) {
        ArtistResponseDTO responseDTO = new ArtistResponseDTO();
        responseDTO.setId(artist.getId());
        responseDTO.setName(artist.getName());
        return responseDTO;
    }

    private Artist mapRequestDTOToArtist(ArtistRequestDTO requestDTO) {
        Artist artist = new Artist();
        artist.setName(requestDTO.getName());
        return artist;
    }
}
