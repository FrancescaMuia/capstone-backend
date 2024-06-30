package it.epicode.viniEVinili.wines;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.viniEVinili.exceptions.NotFoundException;
import it.epicode.viniEVinili.vinyls.Vinyl;
import it.epicode.viniEVinili.vinyls.VinylRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WineService {

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private VinylRepository vinylRepository;

    @Value("${CLOUDINARY_URL}")
    private String cloudinaryUrl;

    public WineResponseDTO findById(Long wineId) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId));
        return mapWineToResponseDTO(wine);
    }

    public WineResponseDTO save(WineRequestDTO requestDTO) {
        Wine wine = mapRequestDTOToWine(requestDTO);
        Wine savedWine = wineRepository.save(wine);
        return mapWineToResponseDTO(savedWine);
    }

    public WineResponseDTO update(Long wineId, WineRequestDTO requestDTO) {
        Wine existingWine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId));
        updateWineFromDTO(existingWine, requestDTO);
        Wine updatedWine = wineRepository.save(existingWine);
        return mapWineToResponseDTO(updatedWine);
    }

    public WineResponseDTO patch(Long wineId, WineRequestDTO requestDTO) {
        Wine existingWine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId));

        if (requestDTO.getVariety() != null) {
            existingWine.setVariety(requestDTO.getVariety());
        }
        if (requestDTO.getProducer() != null) {
            existingWine.setProducer(requestDTO.getProducer());
        }
        if (requestDTO.getDescription() != null) {
            existingWine.setDescription(requestDTO.getDescription());
        }
        if (requestDTO.getRecommendedVinylId() != null && !requestDTO.getRecommendedVinylId().isEmpty()) {
            List<Vinyl> vinyls = requestDTO.getRecommendedVinylId().stream()
                    .map(vinylId -> vinylRepository.findById(vinylId)
                            .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId)))
                    .collect(Collectors.toList());
            existingWine.setRecommendedVinyls(vinyls);
        }

        Wine updatedWine = wineRepository.save(existingWine);
        return mapWineToResponseDTO(updatedWine);
    }

    public void delete(Long wineId) {
        wineRepository.deleteById(wineId);
    }

    public List<WineResponseDTO> findAll() {
        List<Wine> wines = wineRepository.findAll();
        return wines.stream()
                .map(this::mapWineToResponseDTO)
                .collect(Collectors.toList());
    }

    private WineResponseDTO mapWineToResponseDTO(Wine wine) {
        WineResponseDTO responseDTO = new WineResponseDTO();
        responseDTO.setId(wine.getId());
        responseDTO.setVariety(wine.getVariety());
        responseDTO.setProducer(wine.getProducer());
        responseDTO.setDescription(wine.getDescription());
        //responseDTO.setRecommendedVinyls(wine.getRecommendedVinyls());
        responseDTO.setRecommendedVinyls(
                wine.getRecommendedVinyls().stream()
                        .map(vinyl -> {
                            RecommendedResponseDTO recDTO = new RecommendedResponseDTO();
                            recDTO.setId(vinyl.getId());
                            recDTO.setName(vinyl.getName());
                            return recDTO;
                        })
                        .collect(Collectors.toList())
        );
        return responseDTO;
    }

    private Wine mapRequestDTOToWine(WineRequestDTO requestDTO) {
        Wine wine = new Wine();
        wine.setVariety(requestDTO.getVariety());
        wine.setProducer(requestDTO.getProducer());
        wine.setDescription(requestDTO.getDescription());

        List<Vinyl> vinyls = requestDTO.getRecommendedVinylId().stream()
                .map(vinylId -> vinylRepository.findById(vinylId)
                        .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId)))
                .collect(Collectors.toList());
        wine.setRecommendedVinyls(vinyls);
        return wine;
    }

    private void updateWineFromDTO(Wine wine, WineRequestDTO requestDTO) {
        wine.setVariety(requestDTO.getVariety());
        wine.setProducer(requestDTO.getProducer());
        wine.setDescription(requestDTO.getDescription());

        List<Vinyl> vinyls = requestDTO.getRecommendedVinylId().stream()
                .map(vinylId -> vinylRepository.findById(vinylId)
                        .orElseThrow(() -> new EntityNotFoundException("Vinyl not found with id: " + vinylId)))
                .collect(Collectors.toList());
        wine.setRecommendedVinyls(vinyls);
    }

    public Wine saveProductImg(long id, MultipartFile file) throws IOException {
        var wine = wineRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        var url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        wine.setCoverImg(url);
        return wineRepository.save(wine);
    }
}
