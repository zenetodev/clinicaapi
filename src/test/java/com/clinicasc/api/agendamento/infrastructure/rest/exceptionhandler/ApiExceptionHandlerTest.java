package com.clinicasc.api.agendamento.infrastructure.rest.exceptionhandler;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Mock
    private BindingResult bindingResult;

    @Test
    void deveRetornarCamposInvalidosNaRespostaDeValidacao() throws NoSuchMethodException {
        Method metodo = ApiExceptionHandlerTest.class.getDeclaredMethod("metodoDeTeste", String.class);
        MethodParameter parametro = new MethodParameter(metodo, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parametro, bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("input", "motivo", "não pode estar em branco")
        ));

        ApiExceptionHandler.ErroResponse response = handler.handleValidacao(exception).getBody();

        assertEquals("Dados de entrada inválidos.", response.mensagem());
        assertEquals("não pode estar em branco", response.erros().get("motivo"));
    }

    @Test
    void deveRetornarMensagemDaRegraDeNegocio() {
        ApiExceptionHandler.ErroResponse response = handler
                .handleRegraDeNegocio(new RegraDeNegocioException("Consulta já cancelada."))
                .getBody();

        assertEquals("Consulta já cancelada.", response.mensagem());
        assertTrue(response.erros().isEmpty());
    }

    private void metodoDeTeste(String valor) {
    }
}