package com.blog.security.post.services.infra.config;

import com.blog.security.post.services.infra.feign.UserFeignClient;
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
        log.info("[ApplicationConfig] -> (userDetailsService): Criando o UserDetailsService usando UserFeignClient.");
        return username -> {
            var user = userFeignClient.findByEmail(username).getBody();
            if (user == null) {
                log.warn("[ApplicationConfig] -> (userDetailsService): Usuário não encontrado para o email: {}", username);
            }
            return user;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("[ApplicationConfig] -> (authenticationProvider): Criando o AuthenticationProvider com UserDetailsService e PasswordEncoder.");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("[ApplicationConfig] -> (authenticationManager): Criando o AuthenticationManager.");
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("[ApplicationConfig] -> (passwordEncoder): Criando o PasswordEncoder usando BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }
}
