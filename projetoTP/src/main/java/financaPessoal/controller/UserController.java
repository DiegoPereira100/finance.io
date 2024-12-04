package financaPessoal.controller;

import financaPessoal.DTOS.UserRecordDTO;
import financaPessoal.models.UserModel;
import financaPessoal.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuários", description = "Gerenciamento de usuários, incluindo registro e login")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Registrar um novo usuário", description = "Cria uma nova conta de usuário com nome de usuário e senha.")
    public ResponseEntity<Object> registerUser(
            @RequestBody @Valid @Parameter(description = "Dados do usuário para registro", required = true) UserRecordDTO userRecordDTO) {
        if (userRepository.findByUsername(userRecordDTO.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já está em uso.");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDTO, userModel);
        userModel.setPassword(passwordEncoder.encode(userRecordDTO.password()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userModel));
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login de um usuário", description = "Verifica as credenciais de um usuário e retorna o status do login.")
    public ResponseEntity<Object> loginUser(
            @RequestBody @Valid @Parameter(description = "Credenciais do usuário para login", required = true) UserRecordDTO userRecordDTO) {
        Optional<UserModel> userOptional = userRepository.findByUsername(userRecordDTO.username());
        if (userOptional.isEmpty() || !passwordEncoder.matches(userRecordDTO.password(), userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Login bem-sucedido! " + userOptional.get());
    }
}
