package it.epicode.viniEVinili.wines;

import lombok.Data;

@Data
public class RecommendedResponseDTO {
    private Long id;
    private String name;
    private double price;
}
