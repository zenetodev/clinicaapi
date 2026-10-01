package com.clinicasc.api.usuario.application.service;

import com.clinicasc.api.usuario.domain.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {

    private final SecretKey secretKey;
    private final long expirationSeconds;

    public JwtTokenService(@Value("${security.jwt.secret}") String secret,
                           @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT_SECRET deve possuir pelo menos 32 caracteres.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }

    public String gerar(Usuario usuario) {
        Instant agora = Instant.now();
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("email", usuario.getEmail())
                .claim("tipo", usuario.getTipo().name())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(agora.plusSeconds(expirationSeconds)))
                .signWith(secretKey)
                .compact();
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public Jws<Claims> validar(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}