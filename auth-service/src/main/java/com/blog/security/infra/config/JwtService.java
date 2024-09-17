package com.blog.security.infra.config;

import com.blog.security.infra.feign.response.UserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private static final String SECRET_KEY = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";

    public String extractUsername(String token) {
        log.info("[JwtService] -> (extractUsername): Extraindo o nome de usuário do token.");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.info("[JwtService] -> (extractClaim): Extraindo reclamação do token.");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserResponse userDetails) {
        log.info("[JwtService] -> (generateToken): Gerando token para o usuário: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserResponse userDetails) {
        log.info("[JwtService] -> (generateToken): Gerando token com claims adicionais para o usuário: {}", userDetails.getUsername());
        return Jwts.builder()
                   .setClaims(extraClaims)
                   .setSubject(userDetails.getUsername())
                   .claim("userId", userDetails.getId())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                   .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("[JwtService] -> (isTokenValid): Verificando validade do token para o usuário: {}", userDetails.getUsername());
        final String username = extractUsername(token);
        return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        log.info("[JwtService] -> (isTokenExpired): Verificando expiração do token.");
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        log.info("[JwtService] -> (extractExpiration): Extraindo data de expiração do token.");
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        log.info("[JwtService] -> (extractAllClaims): Extraindo todas as reclamações do token.");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        log.info("[JwtService] -> (getSignInKey): Obtendo a chave de assinatura.");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
