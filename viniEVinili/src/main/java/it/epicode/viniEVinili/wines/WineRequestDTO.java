package it.epicode.viniEVinili.wines;

import it.epicode.viniEVinili.vinyls.Vinyl;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class WineRequestDTO {

    private String variety;
    private String producer;
    private String description;
    private Set<Long> recommendedVinylId = new HashSet<>();
}


