package blog.user.services.api.rest.controller;

import blog.user.services.api.rest.dto.request.UserCreateRequestDTO;
import blog.user.services.api.rest.dto.request.UserUpdateRequestDTO;
import blog.user.services.api.rest.dto.response.UserResponseDTO;
import blog.user.services.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/find_by_email")
    public ResponseEntity<UserResponseDTO> findByEmail(@RequestParam(value = "email") String email) {
        log.info("[UserController] -> (findByEmail): Buscando usuário com email '{}'.", email);
        UserResponseDTO userResponse = service.findByEmail(email);
        log.info("[UserController] -> (findByEmail): Usuário encontrado: '{}'.", userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable(value = "id") Long id) {
        log.info("[UserController] -> (findById): Buscando usuário com ID '{}'.", id);
        UserResponseDTO userResponse = service.findById(id);
        log.info("[UserController] -> (findById): Usuário encontrado: '{}'.", userResponse);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateRequestDTO requestDTO) {
        log.info("[UserController] -> (create): Criando usuário com dados '{}'.", requestDTO);
        UserResponseDTO userCreated = service.create(requestDTO);
        log.info("[UserController] -> (create): Usuário criado com sucesso: '{}'.", userCreated);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO,
                                                  @PathVariable(value = "id") Long id) {
        log.info("[UserController] -> (update): Atualizando usuário com ID '{}' e dados '{}'.", id, userUpdateRequestDTO);
        UserResponseDTO userUpdated = service.update(userUpdateRequestDTO, id);
        log.info("[UserController] -> (update): Usuário atualizado com sucesso: '{}'.", userUpdated);
        return ResponseEntity.ok(userUpdated);
    }
}
