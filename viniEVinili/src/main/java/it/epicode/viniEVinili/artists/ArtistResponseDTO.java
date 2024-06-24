package it.epicode.viniEVinili.artists;

import it.epicode.viniEVinili.tracks.TrackResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ArtistResponseDTO {

    private Long id;
    private String name;
    private List<TrackResponseDTO> tracks;
}
