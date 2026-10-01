package com.clinicasc.api.usuario.infrastructure.security;

import com.clinicasc.api.usuario.application.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            autenticar(authorization.substring(7), request);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(String token, HttpServletRequest request) {
        try {
            Jws<Claims> claims = jwtTokenService.validar(token);
            String tipo = claims.getPayload().get("tipo", String.class);
            if (tipo == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                return;
            }

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + tipo));
            var authentication = new UsernamePasswordAuthenticationToken(
                    claims.getPayload().getSubject(),
                    null,
                    authorities
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
        }
    }
}