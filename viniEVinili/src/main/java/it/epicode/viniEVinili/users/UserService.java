package it.epicode.viniEVinili.users;

import it.epicode.viniEVinili.addresses.Address;
import it.epicode.viniEVinili.addresses.AddressRepository;
import it.epicode.viniEVinili.addresses.AddressRequestDTO;
import it.epicode.viniEVinili.addresses.AddressResponseDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<UserResponsePrj> findAllUsers(){
        return userRepository.findAllBy();
    }

    /*public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

     */

    public UserResponseDTO findById(Long userId){
        // Questo metodo cerca un entity nel database utilizzando l'ID.
        // Se l'entity non esiste, viene generata un'eccezione.
        if(!userRepository.existsById(userId)){
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        // Se l'entity esiste, viene recuperato e le sue proprietà vengono copiate in un oggetto PersonaResponse.
        User entity = userRepository.findById(userId).get();
        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(entity, response);

        response.setAddresses(
                entity.getAddresses().stream().map(address -> {
                    AddressResponseDTO addressResponse = new AddressResponseDTO();
                    BeanUtils.copyProperties(address, addressResponse);
                    return addressResponse;
                }).collect(Collectors.toList())
        );

        return response;
    }

    /*public User save(User user) {
        return userRepository.save(user);
    }
     */
    @Transactional
    public UserResponseDTO save(UserRequestDTO request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new EntityExistsException("Lo user esiste gia' ");
        }
        User entity = new User();
        BeanUtils.copyProperties(request, entity);

        userRepository.save(entity);

        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(entity, response);
        //userRepository.save(entity);
        return response;
    }

    /*public User update(User user) {
        // Controlla se l'utente esiste
        if (!userRepository.existsById(user.getId())) {
            throw new EntityNotFoundException("Cannot update user. User with id " + user.getId() + " not found.");
        }
        return userRepository.save(user);
    }
     */
    public UserResponseDTO update(Long id, UserRequestDTO request){
        // Questo metodo modifica un entity esistente.
        // Prima verifica se l'entity esiste nel database. Se non esiste, viene generata un'eccezione.
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("User non trovato");
        }
        // Se l'entity esiste, le sue proprietà vengono modificate con quelle presenti nell'oggetto PersonaRequest.
        User entity = userRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        // L'entity modificato viene quindi salvato nel database e le sue proprietà vengono copiate in un oggetto PersonaResponse.
        userRepository.save(entity);
        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    /*public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
     */
    public String delete(Long id){
        // Questo metodo elimina un Persona dal database.
        // Prima verifica se l'Persona esiste nel database. Se non esiste, viene generata un'eccezione.
        if(!userRepository.existsById(id)){
            throw  new EntityNotFoundException("User non trovato");
        }
        // Se l'Persona esiste, viene eliminato dal database.
        userRepository.deleteById(id);
        return "User eliminato";
    }

    @Transactional
    public AddressResponseDTO addAddress(Long userId, AddressRequestDTO addressRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Address address = new Address();
        //System.out.println("AddressRequestDTO: " + addressRequestDTO);
        BeanUtils.copyProperties(addressRequestDTO, address);
        //System.out.println("Address (after copy): " + address);
        address.setUser(user);

        addressRepository.save(address);
        //System.out.println("Address (after save): " + address);

        AddressResponseDTO response = new AddressResponseDTO();
        BeanUtils.copyProperties(address, response);
        return response;


    }
}
