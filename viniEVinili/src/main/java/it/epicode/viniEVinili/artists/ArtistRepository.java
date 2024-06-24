package it.epicode.viniEVinili.artists;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository <Artist, Long> {
}
