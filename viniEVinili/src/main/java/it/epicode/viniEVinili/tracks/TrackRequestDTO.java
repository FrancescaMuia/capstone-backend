package it.epicode.viniEVinili.tracks;

import it.epicode.viniEVinili.artists.Artist;
import lombok.Data;

import java.util.List;

@Data
public class TrackRequestDTO {

    private String name;
    private int duration;
    private List<Long> artistIds;
}
