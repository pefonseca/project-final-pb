package com.blog.security.infra.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("[JwtAuthenticationFilter] -> (doFilterInternal): Cabeçalho de autorização não encontrado ou não está no formato Bearer. Continuando o filtro.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        log.info("[JwtAuthenticationFilter] -> (doFilterInternal): Token JWT extraído, e-mail do usuário: {}", userEmail);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("[JwtAuthenticationFilter] -> (doFilterInternal): Tentando carregar os detalhes do usuário para o e-mail: {}", userEmail);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)) {
                log.info("[JwtAuthenticationFilter] -> (doFilterInternal): Token JWT é válido. Autenticando usuário.");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("[JwtAuthenticationFilter] -> (doFilterInternal): Contexto de segurança atualizado com a autenticação do usuário.");
            }
        }
        filterChain.doFilter(request, response);
    }
}
