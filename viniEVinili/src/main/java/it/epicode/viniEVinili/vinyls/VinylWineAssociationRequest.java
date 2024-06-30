package it.epicode.viniEVinili.vinyls;

import lombok.Data;

@Data
public class VinylWineAssociationRequest {
    private Long vinylId;
    private Long wineId;
}