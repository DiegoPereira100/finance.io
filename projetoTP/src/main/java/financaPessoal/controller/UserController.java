package financaPessoal.controller;

import financaPessoal.DTOS.UserRecordDTO;
import financaPessoal.models.UserModel;
import financaPessoal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserRecordDTO userRecordDTO) {
        if (userRepository.findByUsername(userRecordDTO.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario já está em uso.");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        userModel.setPassword(passwordEncoder.encode(userRecordDTO.password()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid UserRecordDTO userRecordDTO) {
        Optional<UserModel> userOptional = userRepository.findByUsername(userRecordDTO.username());
        if (userOptional.isEmpty() || !passwordEncoder.matches(userRecordDTO.password(), userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Login bem-sucedido! "+ userOptional.get());
    }
}
