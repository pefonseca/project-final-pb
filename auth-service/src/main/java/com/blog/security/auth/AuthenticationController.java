package com.blog.security.auth;

import com.blog.security.infra.feign.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        log.info("[AuthenticationController] -> (register): Recebendo solicitação de registro para o usuário: {}", request.getEmail());
        var register = service.register(request);
        log.info("[AuthenticationController] -> (register): Registro realizado com sucesso para o usuário: {}", request.getEmail());
        return ResponseEntity.ok(register);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("[AuthenticationController] -> (authenticate): Recebendo solicitação de autenticação para o usuário: {}", request.getEmail());
        var response = service.authenticate(request);
        log.info("[AuthenticationController] -> (authenticate): Autenticação realizada com sucesso para o usuário: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("[AuthenticationController] -> (getUserProfile): Solicitação de perfil de usuário para o usuário: {}", userDetails.getUsername());
        var userResponse = service.findUserByEmail(userDetails.getUsername());
        log.info("[AuthenticationController] -> (getUserProfile): Perfil de usuário retornado para o usuário: {}", userDetails.getUsername());
        return ResponseEntity.ok(userResponse);
    }
}