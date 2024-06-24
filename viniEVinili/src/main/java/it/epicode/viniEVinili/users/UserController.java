package it.epicode.viniEVinili.users;

import it.epicode.viniEVinili.addresses.AddressRequestDTO;
import it.epicode.viniEVinili.addresses.AddressResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> modifyUser (@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO userResponseDTO = user.update(id,userRequestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        String message = user.delete(id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        UserResponseDTO response = user.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/address")
    public ResponseEntity<AddressResponseDTO> addAddress(@PathVariable Long userId, @RequestBody AddressRequestDTO addressRequestDTO) {
        AddressResponseDTO addressResponseDTO = user.addAddress(userId, addressRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponseDTO);
    }
}
