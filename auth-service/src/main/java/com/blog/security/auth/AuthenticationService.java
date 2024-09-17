package com.blog.security.auth;

import com.blog.security.infra.config.JwtService;
import com.blog.security.infra.request.UserRequest;
import com.blog.security.infra.feign.response.UserResponse;
import com.blog.security.infra.service.UserFeignService;
import com.blog.security.infra.user.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Qualifier("userFeignService")
    private final UserFeignService service;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        log.info("[AuthenticationService] -> (register): Iniciando registro para o usuário: {}", request.getEmail());

        UserRequest user = UserRequest.builder()
                                      .firstName(request.getFirstname())
                                      .lastName(request.getLastname())
                                      .email(request.getEmail())
                                      .password(passwordEncoder.encode(request.getPassword()))
                                      .bio(request.getBio())
                                      .city(request.getCity())
                                      .role(Role.USER)
                                      .build();

        log.info("[AuthenticationService] -> (register): Criando usuário com e-mail: {}", request.getEmail());
        UserResponse userDB = service.create(user);

        log.info("[AuthenticationService] -> (register): Usuário criado com sucesso. Gerando token JWT.");
        String jwtToken = jwtService.generateToken(userDB);

        log.info("[AuthenticationService] -> (register): Registro concluído com sucesso para o usuário: {}", request.getEmail());
        return AuthenticationResponse.builder()
                                     .token(jwtToken)
                                     .build();
    }

    public UserResponse findUserByEmail(String email) {
        log.info("[AuthenticationService] -> (findUserByEmail): Buscando usuário com e-mail: {}", email);
        var userResponse = service.findByEmail(email);
        log.info("[AuthenticationService] -> (findUserByEmail): Usuário encontrado com e-mail: {}", email);
        return userResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("[AuthenticationService] -> (authenticate): Iniciando autenticação para o usuário: {}", request.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            log.info("[AuthenticationService] -> (authenticate): Autenticação bem-sucedida para o usuário: {}", request.getEmail());
            UserResponse user = service.findByEmail(request.getEmail());
            String jwtToken = jwtService.generateToken(user);

            log.info("[AuthenticationService] -> (authenticate): Token JWT gerado para o usuário: {}", request.getEmail());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            log.error("[AuthenticationService] -> (authenticate): Falha na autenticação para o usuário: {}. Erro: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Authentication failed");
        }
    }
}
