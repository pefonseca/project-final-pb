package com.blog.security.auth;

import com.blog.security.infra.config.JwtService;
import com.blog.security.infra.request.UserRequest;
import com.blog.security.infra.feign.response.UserResponse;
import com.blog.security.infra.service.UserFeignService;
import com.blog.security.infra.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Qualifier("userFeignService")
    private final UserFeignService service;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        UserRequest user = UserRequest.builder()
                                      .firstName(request.getFirstname())
                                      .lastName(request.getLastname())
                                      .email(request.getEmail())
                                      .password(passwordEncoder.encode(request.getPassword()))
                                      .bio(request.getBio())
                                      .city(request.getCity())
                                      .role(Role.USER)
                                      .build();

        UserResponse userDB = service.create(user);

        String jwtToken = jwtService.generateToken(userDB);

        return AuthenticationResponse.builder()
                                     .token(jwtToken)
                                     .build();
    }

    public UserResponse findUserByEmail(String email) {
        return service.findByEmail(email);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserResponse user = service.findByEmail(request.getEmail());
            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed");
        }
    }
}
