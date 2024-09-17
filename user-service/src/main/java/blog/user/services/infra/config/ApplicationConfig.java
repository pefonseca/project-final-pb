package blog.user.services.infra.config;

import blog.user.services.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        log.info("[ApplicationConfig] -> (userDetailsService): Configurando UserDetailsService.");
        return username -> repository.findByEmail(username)
                                     .orElseThrow(() -> {
                                         log.error("[ApplicationConfig] -> (userDetailsService): Usuário não encontrado para o e-mail: {}", username);
                                         return new UsernameNotFoundException("user not found");
                                     });
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        log.info("[ApplicationConfig] -> (authenticationProvider): Configurando AuthenticationProvider.");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("[ApplicationConfig] -> (authenticationManager): Configurando AuthenticationManager.");
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("[ApplicationConfig] -> (passwordEncoder): Configurando PasswordEncoder.");
        return new BCryptPasswordEncoder();
    }
}
