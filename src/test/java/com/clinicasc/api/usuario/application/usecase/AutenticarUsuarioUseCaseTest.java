package com.clinicasc.api.usuario.application.usecase;

import com.clinicasc.api.usuario.application.dto.LoginInput;
import com.clinicasc.api.usuario.application.dto.LoginOutput;
import com.clinicasc.api.usuario.application.service.JwtTokenService;
import com.clinicasc.api.usuario.domain.exception.FalhaAutenticacaoException;
import com.clinicasc.api.usuario.domain.model.TipoUsuario;
import com.clinicasc.api.usuario.domain.model.Usuario;
import com.clinicasc.api.usuario.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AutenticarUsuarioUseCase useCase;

    @Test
    void deveAutenticarUsuarioEEmitirToken() {
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "hash-da-senha", TipoUsuario.PACIENTE);
        when(usuarioRepository.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-segura", "hash-da-senha")).thenReturn(true);
        when(jwtTokenService.gerar(usuario)).thenReturn("token-jwt");
        when(jwtTokenService.getExpirationSeconds()).thenReturn(3600L);

        LoginOutput output = useCase.executar(new LoginInput(" MARIA@EXAMPLE.COM ", "senha-segura"));

        assertEquals("token-jwt", output.token());
        assertEquals(3600L, output.expiresInSeconds());
    }

    @Test
    void deveRejeitarSenhaInvalida() {
        Usuario usuario = new Usuario("Maria Silva", "maria@example.com", "hash-da-senha", TipoUsuario.PACIENTE);
        when(usuarioRepository.buscarPorEmail("maria@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-errada", "hash-da-senha")).thenReturn(false);

        assertThrows(FalhaAutenticacaoException.class,
                () -> useCase.executar(new LoginInput("maria@example.com", "senha-errada")));
    }

    @Test
    void deveRejeitarEmailNaoCadastrado() {
        when(usuarioRepository.buscarPorEmail("naoexiste@example.com")).thenReturn(Optional.empty());

        assertThrows(FalhaAutenticacaoException.class,
                () -> useCase.executar(new LoginInput("naoexiste@example.com", "senha-segura")));
    }
}