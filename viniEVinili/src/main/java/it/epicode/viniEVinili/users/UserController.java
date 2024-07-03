package it.epicode.viniEVinili.users;


import it.epicode.viniEVinili.exceptions.UserAlreadyExistsException;
import it.epicode.viniEVinili.security.ApiValidationException;
import it.epicode.viniEVinili.security.LoginModel;
import it.epicode.viniEVinili.security.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService user;

    @Autowired
    private UserRepository usersRepository;

    @GetMapping
    public List<UserResponsePrj> getAllUsers() {
        return user.findAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser (@RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO userResponseDTO = user.save(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);

    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<UserRequestDTO> registerAdmin(@RequestBody UserRequestDTO registerUser){
        return ResponseEntity.ok(user.registerAdmin(registerUser));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRequestDTO> register(@RequestBody @Validated UserRequestDTO register, BindingResult validator){
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        try {
        var registeredUser = user.register(
                UserRequestDTO.builder()
                        .withName(register.getName())
                        .withSurname(register.getSurname())
                        .withUsername(register.getUsername())
                        .withEmail(register.getEmail())
                        .withPassword(register.getPassword())
                        .withCity(register.getCity())
                        .withToponym(register.getToponym())
                        .withAddressName(register.getAddressName())
                        .withStreetNumber(register.getStreetNumber())
                        .withZipCode(register.getZipCode())
                        .withPhoneNumber(register.getPhoneNumber())
                        .build());

        return  new ResponseEntity<> (registeredUser, HttpStatus.OK);
    } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(user.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<UserResponseDTO> modifyUser (@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO){
//
//        UserResponseDTO userResponseDTO = user.update(id,userRequestDTO);
//        return ResponseEntity.ok(userResponseDTO);
//    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> modifyUser (@RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO userResponseDTO = user.update(userRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String message = user.delete(id);
        return ResponseEntity.ok(message);
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO response = user.getUserById(id);
        return ResponseEntity.ok(response);
    }

     */

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        Optional<UserResponseDTO> userDTO = user.getUserById(id);
        return userDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /*@PostMapping("/{userId}/address")
    public ResponseEntity<AddressResponseDTO> addAddress(@PathVariable Long userId, @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO addressResponseDTO = user.updateAddress(userId, addressRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponseDTO);
    }

     */

    @PatchMapping("/{id}/avatar")
    public User uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable Long id) {
        try {
            return user.saveAvatar(id, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
