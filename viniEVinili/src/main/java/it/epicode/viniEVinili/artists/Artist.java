package it.epicode.viniEVinili.artists;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.epicode.viniEVinili.tracks.Track;
import it.epicode.viniEVinili.vinyls.Vinyl;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /*@ManyToMany(mappedBy = "artists")
    private List<Vinyl> vinyls;

     */

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "track_artist",
            joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks;
}
