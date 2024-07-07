package it.epicode.viniEVinili.wines;

import it.epicode.viniEVinili.vinyls.Vinyl;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class WineResponseDTO {

    private Long id;
    private String variety;
    private String producer;
    private String description;
    private double price;
    private boolean available;
    //private List<Vinyl> recommendedVinyls;
    private List<RecommendedResponseDTO> recommendedVinyls;
}
