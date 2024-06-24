package it.epicode.viniEVinili.tracks;


import it.epicode.viniEVinili.artists.Artist;
import it.epicode.viniEVinili.artists.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public TrackResponseDTO findById(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + trackId));
        return mapTrackToResponseDTO(track);
    }

    public TrackResponseDTO save(TrackRequestDTO requestDTO) {
        Track track = mapRequestDTOToTrack(requestDTO);
        Track savedTrack = trackRepository.save(track);
        return mapTrackToResponseDTO(savedTrack);
    }

    public TrackResponseDTO update(Long trackId, TrackRequestDTO requestDTO) {
        Track existingTrack = trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + trackId));

        // Aggiorna i campi del track esistente
        existingTrack.setName(requestDTO.getName());
        existingTrack.setDuration(requestDTO.getDuration());

        // Mappa gli ID degli artisti a oggetti Artist
        List<Artist> artists = requestDTO.getArtistIds().stream()
                .map(id -> artistRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id)))
                .collect(Collectors.toList());

        // Imposta la lista di artisti aggiornata sul track esistente
        existingTrack.setArtists(artists);

        // Salva il track aggiornato nel repository
        Track updatedTrack = trackRepository.save(existingTrack);

        // Ritorna il DTO di risposta mappato dal track aggiornato
        return mapTrackToResponseDTO(updatedTrack);
    }

    public void delete(Long trackId) {
        trackRepository.deleteById(trackId);
    }

    public List<TrackResponseDTO> findAll() {
        List<Track> tracks = trackRepository.findAll();
        return tracks.stream()
                .map(this::mapTrackToResponseDTO)
                .collect(Collectors.toList());
    }

    private TrackResponseDTO mapTrackToResponseDTO(Track track) {
        TrackResponseDTO responseDTO = new TrackResponseDTO();
        responseDTO.setId(track.getId());
        responseDTO.setName(track.getName());
        responseDTO.setDuration(track.getDuration());
        responseDTO.setArtists(track.getArtists());
        return responseDTO;
    }

    private Track mapRequestDTOToTrack(TrackRequestDTO requestDTO) {
        Track track = new Track();
        track.setName(requestDTO.getName());
        track.setDuration(requestDTO.getDuration());

        List<Artist> artists = requestDTO.getArtistIds().stream()
                .map(id -> artistRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Artist not found with id: " + id)))
                .collect(Collectors.toList());

        track.setArtists(artists);

        return track;
    }
}
