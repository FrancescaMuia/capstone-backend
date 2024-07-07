package it.epicode.viniEVinili.wines;

import it.epicode.viniEVinili.vinyls.Vinyl;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class WineRequestDTO {

    private String variety;
    private String producer;
    private String description;
    private double price;
    private boolean available;
    private List<Long> recommendedVinylId = new ArrayList<>();
}


