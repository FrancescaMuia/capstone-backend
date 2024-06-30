package it.epicode.viniEVinili.vinyls;

import it.epicode.viniEVinili.artists.Artist;
import it.epicode.viniEVinili.tracks.Track;
import it.epicode.viniEVinili.wines.Wine;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class VinylResponseDTO {

    private Long id;
    private String name;
    private double price;
    private boolean available;
    private int year;

    private List<Track> tracks;
    private String coverImg;
    private String genre;
    //private List<Wine> recommendedWines;
    private List<RecommendedResponseDTO> recommendedWines;
}
