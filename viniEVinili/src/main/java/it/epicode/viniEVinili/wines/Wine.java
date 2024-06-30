package it.epicode.viniEVinili.wines;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.vinyls.Vinyl;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "wines")
public class Wine extends Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String variety;
    private String producer;
    private String description;
    private String coverImg;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "vinyl_wine",
            joinColumns = @JoinColumn(name = "wine_id"),
            inverseJoinColumns = @JoinColumn(name = "vinyl_id")
    )
    private List<Vinyl> recommendedVinyls;
}
