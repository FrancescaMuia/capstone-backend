package it.epicode.viniEVinili.vinyls;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.epicode.viniEVinili.artists.Artist;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.tracks.Track;
import it.epicode.viniEVinili.wines.Wine;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vinyls")
public class Vinyl extends Product {



    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "vinyl_track",
            joinColumns = @JoinColumn(name = "vinyl_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks;

    private String coverImg;
    private String genre;


//    @JsonManagedReference
//    @ManyToMany(mappedBy = "recommendedVinyls", fetch = FetchType.EAGER)
//    private List<Wine> recommendedWines;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "vinyl_wine",
            joinColumns = @JoinColumn(name = "vinyl_id"),
            inverseJoinColumns = @JoinColumn(name = "wine_id")
    )
    private List<Wine> recommendedWines;
}
