package it.epicode.viniEVinili.vinyls;

import it.epicode.viniEVinili.artists.Artist;
import it.epicode.viniEVinili.tracks.Track;
import it.epicode.viniEVinili.wines.Wine;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class VinylRequestDTO {

    private String name;
    private double price;
    private boolean available;
    private int year;
    //private List<Artist> artists;
    private List<Long> trackIds = new ArrayList<>();
    private String coverImg;
    private String genre;
    //private Set<Wine> recommendedWines;
    private List<Long> recommendedWineIds = new ArrayList<>();
}
