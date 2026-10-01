package com.clinicasc.api.usuario.application.service;

import com.clinicasc.api.usuario.domain.model.TipoUsuario;
import com.clinicasc.api.usuario.domain.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtTokenServiceTest {

    private static final String SECRET = "uma-chave-secreta-com-pelo-menos-32-caracteres";

    @Test
    void deveGerarTokenComIdentidadeETipoDoUsuario() {
        JwtTokenService service = new JwtTokenService(SECRET, 3600L);
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "hash-da-senha", TipoUsuario.PACIENTE);

        String token = service.gerar(usuario);
        SecretKey key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        assertEquals(usuario.getId().toString(), claims.getSubject());
        assertEquals(usuario.getEmail(), claims.get("email", String.class));
        assertEquals("PACIENTE", claims.get("tipo", String.class));
    }
}