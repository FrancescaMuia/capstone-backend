package it.epicode.viniEVinili.wines;

import it.epicode.viniEVinili.vinyls.Vinyl;
import lombok.Data;

import java.util.Set;

@Data
public class WineResponseDTO {

    private Long id;
    private String variety;
    private String producer;
    private String description;
    private Set<Vinyl> recommendedVinyls;
}
