package com.blog.security.infra.config;

import com.blog.security.infra.feign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserFeignClient userFeignClient;

    @Bean
    public UserDetailsService userDetailsService() {
        log.info("[SecurityConfig] -> (userDetailsService): Criando UserDetailsService.");
        return username -> {
            log.info("[SecurityConfig] -> (userDetailsService): Buscando usuário com e-mail: {}", username);
            return userFeignClient.findByEmail(username).getBody();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("[SecurityConfig] -> (authenticationProvider): Criando AuthenticationProvider.");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        log.info("[SecurityConfig] -> (authenticationProvider): AuthenticationProvider configurado com UserDetailsService e PasswordEncoder.");
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("[SecurityConfig] -> (authenticationManager): Criando AuthenticationManager.");
        AuthenticationManager authManager = config.getAuthenticationManager();
        log.info("[SecurityConfig] -> (authenticationManager): AuthenticationManager criado com sucesso.");
        return authManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("[SecurityConfig] -> (passwordEncoder): Criando PasswordEncoder.");
        return new BCryptPasswordEncoder();
    }
}
