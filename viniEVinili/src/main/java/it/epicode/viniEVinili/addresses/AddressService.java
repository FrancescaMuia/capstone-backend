package it.epicode.viniEVinili.addresses;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public AddressResponseDTO findById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + addressId));
        return mapAddressToResponseDTO(address);
    }

    public AddressResponseDTO save(AddressRequestDTO requestDTO) {
        Address address = mapRequestDTOToAddress(requestDTO);
        Address savedAddress = addressRepository.save(address);
        return mapAddressToResponseDTO(savedAddress);
    }

    public AddressResponseDTO update(Long addressId, AddressRequestDTO requestDTO) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + addressId));
        existingAddress.setCity(requestDTO.getCity());
        existingAddress.setToponym(requestDTO.getToponym());
        existingAddress.setName(requestDTO.getName());
        existingAddress.setStreetNumber(requestDTO.getStreetNumber());
        existingAddress.setZipCode(requestDTO.getZipCode());

        Address updatedAddress = addressRepository.save(existingAddress);
        return mapAddressToResponseDTO(updatedAddress);
    }

    public void delete(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    public List<AddressResponseDTO> findAll() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(this::mapAddressToResponseDTO)
                .collect(Collectors.toList());
    }

    private AddressResponseDTO mapAddressToResponseDTO(Address address) {
        AddressResponseDTO responseDTO = new AddressResponseDTO();
        responseDTO.setId(address.getId());
        responseDTO.setCity(address.getCity());
        responseDTO.setToponym(address.getToponym());
        responseDTO.setName(address.getName());
        responseDTO.setStreetNumber(address.getStreetNumber());
        responseDTO.setZipCode(address.getZipCode());
        return responseDTO;
    }

    private Address mapRequestDTOToAddress(AddressRequestDTO requestDTO) {
        Address address = new Address();
        address.setCity(requestDTO.getCity());
        address.setToponym(requestDTO.getToponym());
        address.setName(requestDTO.getName());
        address.setStreetNumber(requestDTO.getStreetNumber());
        address.setZipCode(requestDTO.getZipCode());
        return address;
    }
}
