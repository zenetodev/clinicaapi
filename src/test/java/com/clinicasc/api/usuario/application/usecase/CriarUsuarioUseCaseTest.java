package com.clinicasc.api.usuario.application.usecase;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.usuario.application.dto.CriarUsuarioInput;
import com.clinicasc.api.usuario.application.dto.UsuarioOutput;
import com.clinicasc.api.usuario.domain.model.TipoUsuario;
import com.clinicasc.api.usuario.domain.model.Usuario;
import com.clinicasc.api.usuario.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CriarUsuarioUseCase useCase;

    @Test
    void deveCriarUsuarioNormalizandoEmail() {
        Usuario usuarioSalvo = new Usuario("Maria Silva", "maria@example.com", TipoUsuario.PACIENTE);
        when(usuarioRepository.existePorEmail("maria@example.com")).thenReturn(false);
        when(usuarioRepository.salvar(org.mockito.ArgumentMatchers.any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioOutput output = useCase.executar(
                new CriarUsuarioInput("Maria Silva", "  MARIA@EXAMPLE.COM ", TipoUsuario.PACIENTE)
        );

        assertEquals("maria@example.com", output.email());
        assertEquals(TipoUsuario.PACIENTE, output.tipo());
        verify(usuarioRepository).existePorEmail(eq("maria@example.com"));
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        when(usuarioRepository.existePorEmail("maria@example.com")).thenReturn(true);

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(
                new CriarUsuarioInput("Maria Silva", "maria@example.com", TipoUsuario.PACIENTE)
        ));
    }
}