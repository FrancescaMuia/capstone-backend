package it.epicode.viniEVinili.wines;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.epicode.viniEVinili.products.Product;
import it.epicode.viniEVinili.vinyls.Vinyl;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

   /* @ManyToMany
    @JoinTable(name = "vinyl_wine",
            joinColumns = @JoinColumn(name = "wine_id"),
            inverseJoinColumns = @JoinColumn(name = "vinyl_id"))


    */
   @JsonBackReference
   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
           name = "vinyl_wine",
           joinColumns = @JoinColumn(name = "wine_id"),
           inverseJoinColumns = @JoinColumn(name = "vinyl_id")
   )
    private Set<Vinyl> recommendedVinyls;
}
