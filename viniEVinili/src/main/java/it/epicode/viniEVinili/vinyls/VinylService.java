package it.epicode.viniEVinili.vinyls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.viniEVinili.exceptions.NotFoundException;
import it.epicode.viniEVinili.tracks.Track;
import it.epicode.viniEVinili.tracks.TrackRepository;
import it.epicode.viniEVinili.users.User;
import it.epicode.viniEVinili.wines.Wine;
import it.epicode.viniEVinili.wines.WineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VinylService {

    @Autowired
    private VinylRepository vinylRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private WineRepository wineRepository;

    @Value("${CLOUDINARY_URL}")
    private String cloudinaryUrl;

    public VinylResponseDTO findById(Long vinylId) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId));
        return mapVinylToResponseDTO(vinyl);
    }

    public VinylResponseDTO save(VinylRequestDTO requestDTO) {
        Vinyl vinyl = mapRequestDTOToVinyl(requestDTO);
        Vinyl savedVinyl = vinylRepository.save(vinyl);
        return mapVinylToResponseDTO(savedVinyl);
    }

    public VinylResponseDTO update(Long vinylId, VinylRequestDTO requestDTO) {
        Vinyl existingVinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId));
        existingVinyl.setName(requestDTO.getName());
        existingVinyl.setPrice(requestDTO.getPrice());
        existingVinyl.setAvailable(requestDTO.isAvailable());
        existingVinyl.setYear(requestDTO.getYear());

        List<Track> tracks = requestDTO.getTrackIds().stream()
                .map(trackId -> trackRepository.findById(trackId)
                        .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + trackId)))
                .collect(Collectors.toList());
        existingVinyl.setTracks(tracks);

        existingVinyl.setCoverImg(requestDTO.getCoverImg());
        existingVinyl.setGenre(requestDTO.getGenre());

        List<Wine> wines = requestDTO.getRecommendedWineIds().stream()
                .map(wineId -> wineRepository.findById(wineId)
                        .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId)))
                .collect(Collectors.toList());
        existingVinyl.setRecommendedWines(wines);

        Vinyl updatedVinyl = vinylRepository.save(existingVinyl);
        return mapVinylToResponseDTO(updatedVinyl);
    }

    public VinylResponseDTO patch(Long vinylId, VinylRequestDTO requestDTO) {
        Vinyl existingVinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId));

        if (requestDTO.getName() != null) {
            existingVinyl.setName(requestDTO.getName());
        }
        if (requestDTO.getPrice() != 0) {  // assuming price cannot be 0
            existingVinyl.setPrice(requestDTO.getPrice());
        }
        if (requestDTO.isAvailable()) {
            existingVinyl.setAvailable(requestDTO.isAvailable());
        }
        if (requestDTO.getYear() != 0) {  // assuming year cannot be 0
            existingVinyl.setYear(requestDTO.getYear());
        }
        if (requestDTO.getTrackIds() != null && !requestDTO.getTrackIds().isEmpty()) {
            List<Track> tracks = requestDTO.getTrackIds().stream()
                    .map(trackId -> trackRepository.findById(trackId)
                            .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + trackId)))
                    .collect(Collectors.toList());
            existingVinyl.setTracks(tracks);
        }
        if (requestDTO.getCoverImg() != null) {
            existingVinyl.setCoverImg(requestDTO.getCoverImg());
        }
        if (requestDTO.getGenre() != null) {
            existingVinyl.setGenre(requestDTO.getGenre());
        }
        if (requestDTO.getRecommendedWineIds() != null && !requestDTO.getRecommendedWineIds().isEmpty()) {
            List<Wine> wines = requestDTO.getRecommendedWineIds().stream()
                    .map(wineId -> wineRepository.findById(wineId)
                            .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId)))
                    .collect(Collectors.toList());
            existingVinyl.setRecommendedWines(wines);
        }

        Vinyl updatedVinyl = vinylRepository.save(existingVinyl);
        return mapVinylToResponseDTO(updatedVinyl);
    }


    public void delete(Long vinylId) {
        vinylRepository.deleteById(vinylId);
    }

    public List<VinylResponseDTO> findAll() {
        List<Vinyl> vinyls = vinylRepository.findAll();
        return vinyls.stream()
                .map(this::mapVinylToResponseDTO)
                .collect(Collectors.toList());
    }

    private VinylResponseDTO mapVinylToResponseDTO(Vinyl vinyl) {
        VinylResponseDTO responseDTO = new VinylResponseDTO();
        responseDTO.setId(vinyl.getId());
        responseDTO.setName(vinyl.getName());
        responseDTO.setPrice(vinyl.getPrice());
        responseDTO.setAvailable(vinyl.isAvailable());
        responseDTO.setYear(vinyl.getYear());
        responseDTO.setTracks(vinyl.getTracks());
        responseDTO.setCoverImg(vinyl.getCoverImg());
        responseDTO.setGenre(vinyl.getGenre());
        //responseDTO.setRecommendedWines(vinyl.getRecommendedWines());
        responseDTO.setRecommendedWines(
                vinyl.getRecommendedWines().stream()
                        .map(wine -> {
                            RecommendedResponseDTO recDTO = new RecommendedResponseDTO();
                            recDTO.setId(wine.getId());
                            recDTO.setName(wine.getVariety());
                            recDTO.setPrice(wine.getPrice());
                            return recDTO;
                        })
                        .collect(Collectors.toList())
        );
        return responseDTO;
    }

    private Vinyl mapRequestDTOToVinyl(VinylRequestDTO requestDTO) {
        Vinyl vinyl = new Vinyl();
        vinyl.setName(requestDTO.getName());
        vinyl.setPrice(requestDTO.getPrice());
        vinyl.setAvailable(requestDTO.isAvailable());
        vinyl.setYear(requestDTO.getYear());

        List<Track> tracks = requestDTO.getTrackIds().stream()
                .map(trackId -> trackRepository.findById(trackId)
                        .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + trackId)))
                .collect(Collectors.toList());
        vinyl.setTracks(tracks);

        vinyl.setCoverImg(requestDTO.getCoverImg());
        vinyl.setGenre(requestDTO.getGenre());

        List<Wine> wines = requestDTO.getRecommendedWineIds().stream()
                .map(wineId -> wineRepository.findById(wineId)
                        .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId)))
                .collect(Collectors.toList());
        vinyl.setRecommendedWines(wines);

        return vinyl;
    }

//    public void associateVinylAndWine(Long vinylId, Long wineId) {
//        Vinyl vinyl = vinylRepository.findById(vinylId)
//                .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId));
//        Wine wine = wineRepository.findById(wineId)
//                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId));
//
//        // Aggiungi il vino alla lista dei vini suggeriti del vinile
//        vinyl.getRecommendedWines().add(wine);
//
//        // Aggiungi il vinile alla lista dei vinili suggeriti del vino
//        wine.getRecommendedVinyls().add(vinyl);
//
//        // Salva le entità aggiornate
//        vinylRepository.save(vinyl);
//        wineRepository.save(wine);
//    }

    public void associateVinylAndWine(Long vinylId, Long wineId) {
        Vinyl vinyl = vinylRepository.findById(vinylId)
                .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId));
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId));

        vinyl.getRecommendedWines().add(wine);
        wine.getRecommendedVinyls().add(vinyl);

        vinylRepository.save(vinyl);
        wineRepository.save(wine);
    }


    public Vinyl saveProductImg(long id, MultipartFile file) throws IOException {
        var vinyl = vinylRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        var url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        vinyl.setCoverImg(url);
        return vinylRepository.save(vinyl);
    }
}
