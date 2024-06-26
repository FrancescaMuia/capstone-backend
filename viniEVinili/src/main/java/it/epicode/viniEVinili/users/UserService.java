package it.epicode.viniEVinili.users;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.viniEVinili.addresses.Address;
import it.epicode.viniEVinili.addresses.AddressRepository;
import it.epicode.viniEVinili.addresses.AddressRequestDTO;
import it.epicode.viniEVinili.addresses.AddressResponseDTO;
import it.epicode.viniEVinili.email.EmailService;
import it.epicode.viniEVinili.exceptions.NotFoundException;
import it.epicode.viniEVinili.security.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final RolesRepository rolesRepository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;

    @Autowired
    private UserRepository userRepository;

    /*@Autowired
    private AddressRepository addressRepository;

     */

    @Value("${CLOUDINARY_URL}")
    private String cloudinaryUrl;

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

        /*response.setAddresses(
                entity.getAddresses().stream().map(address -> {
                    AddressResponseDTO addressResponse = new AddressResponseDTO();
                    BeanUtils.copyProperties(address, addressResponse);
                    return addressResponse;
                }).collect(Collectors.toList())
        );

         */

        return response;
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToResponse);
    }

    private UserResponseDTO convertToResponse(User user) {
        UserResponseDTO dto = UserResponseDTO.builder()
                .withId(user.getId())
                .withName(user.getName())
                .withSurname(user.getSurname())
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withCity(user.getCity())
                .withToponym(user.getToponym())
                .withAddressName(user.getAddressName())
                .withStreetNumber(user.getStreetNumber())
                .withZipCode(user.getZipCode())
                .withPhoneNumber(user.getPhoneNumber())
//                .withOrders(user.getOrders())
                .withRoles(user.getRoles())
                .build();
        return dto;
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

    public UserRequestDTO registerAdmin(UserRequestDTO register){
        if(userRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }

        Roles roles = rolesRepository.findById(Roles.ROLES_ADMIN).get();
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        userRepository.save(u);
        UserRequestDTO response = new UserRequestDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        return response;

    }

    public UserRequestDTO register(UserRequestDTO register){
        if(userRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }

        Roles roles = rolesRepository.findById(Roles.ROLES_USER).get();
        /*
        if(!rolesRepository.existsById(Roles.ROLES_USER)){
            roles = new Roles();
            roles.setRoleType(Roles.ROLES_USER);
        } else {
            roles = rolesRepository.findById(Roles.ROLES_USER).get();
        }

         */
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        userRepository.save(u);
        UserRequestDTO response = new UserRequestDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(u.getEmail());

        return response;

    }


    /*public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            //SI EFFETTUA IL LOGIN
            //SI CREA UNA AUTENTICAZIONE OVVERO L'OGGETTO DI TIPO AUTHENTICATION
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            a.getAuthorities(); //SERVE A RECUPERARE I RUOLI/IL RUOLO

            //SI CREA UN CONTESTO DI SICUREZZA CHE SARA UTILIZZATO IN PIU OCCASIONI
            SecurityContextHolder.getContext().setAuthentication(a);

            /*var user = userRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponseDTO.builder()
                    .withUser(UserRequestDTO.builder()
                            .withId(user.getId())
                            .withName(user.getName())
                            .withSurname(user.getSurname())
                            .withEmail(user.getEmail())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .build())
                    .build();

             */

/*
            var user = userRepository.findOneByUsername(username).orElseThrow();
            var dto = LoginResponseDTO.builder()
                    .withUser(RegisteredUserDTO.builder()
                            .withId(user.getId())
                            .withName(user.getName())
                            .withSurname(user.getSurname())
                            .withEmail(user.getEmail())
                            .withRoles(user.getRoles())
                            .withUsername(user.getUsername())
                            .build())
                    .build();



            //UTILIZZO DI JWTUTILS PER GENERARE IL TOKEN UTILIZZANDO UNA AUTHENTICATION E LO ASSEGNA ALLA LOGINRESPONSEDTO
            dto.setToken(jwt.generateToken(a));

            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            //ECCEZIONE LANCIATA SE LO USERNAME E SBAGLIATO E QUINDI L'UTENTE NON VIENE TROVATO
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            //ECCEZIONE LANCIATA SE LA PASSWORD E SBAGLIATA
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
    }
    */
@Autowired
private JwtUtils jwtUtils;

    public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            Authentication authentication = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUserDetails userPrincipal = (SecurityUserDetails) authentication.getPrincipal();

            LoginResponseDTO dto = LoginResponseDTO.builder()
                    .withUser(buildRegisteredUserDTO(userPrincipal))
                    .build();

            // Genera il token JWT includendo le informazioni dell'utente
            dto.setToken(jwtUtils.generateToken(authentication));

            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }


    }

    private RegisteredUserDTO buildRegisteredUserDTO(SecurityUserDetails userDetails) {
        RegisteredUserDTO userDto = new RegisteredUserDTO();
        userDto.setId(userDetails.getUserId());
        userDto.setEmail(userDetails.getEmail());
        userDto.setRoles(userDetails.getRoles());
        userDto.setUsername(userDetails.getUsername());
        userDto.setName(userDetails.getName());
        userDto.setSurname(userDetails.getSurname());
        // Aggiungi altri campi se necessario

        return userDto;


    }


    public User saveAvatar(long id, MultipartFile file) throws IOException {
        var user = userRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
        var url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        user.setAvatar(url);
        return userRepository.save(user);
    }
}
