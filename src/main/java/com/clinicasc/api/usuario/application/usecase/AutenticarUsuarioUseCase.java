package com.clinicasc.api.usuario.application.usecase;

import com.clinicasc.api.usuario.application.dto.LoginInput;
import com.clinicasc.api.usuario.application.dto.LoginOutput;
import com.clinicasc.api.usuario.application.service.JwtTokenService;
import com.clinicasc.api.usuario.domain.exception.FalhaAutenticacaoException;
import com.clinicasc.api.usuario.domain.model.Usuario;
import com.clinicasc.api.usuario.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AutenticarUsuarioUseCase(UsuarioRepository usuarioRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtTokenService jwtTokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public LoginOutput executar(LoginInput input) {
        String email = input.email().trim().toLowerCase(Locale.ROOT);
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(FalhaAutenticacaoException::new);

        if (!passwordEncoder.matches(input.senha(), usuario.getSenhaHash())) {
            throw new FalhaAutenticacaoException();
        }

        return new LoginOutput(jwtTokenService.gerar(usuario), jwtTokenService.getExpirationSeconds());
    }
}