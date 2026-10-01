package com.clinicasc.api.agendamento.infrastructure.rest.exceptionhandler;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.exception.AcessoNegadoException;
import com.clinicasc.api.usuario.domain.exception.FalhaAutenticacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResponse> handleRegraDeNegocio(RegraDeNegocioException exception) {
        ErroResponse erro = new ErroResponse(exception.getMessage(), OffsetDateTime.now(), Map.of());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponse> handleAcessoNegado(AcessoNegadoException exception) {
        ErroResponse erro = new ErroResponse(exception.getMessage(), OffsetDateTime.now(), Map.of());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(FalhaAutenticacaoException.class)
    public ResponseEntity<ErroResponse> handleFalhaAutenticacao(FalhaAutenticacaoException exception) {
        ErroResponse erro = new ErroResponse(exception.getMessage(), OffsetDateTime.now(), Map.of());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidacao(MethodArgumentNotValidException exception) {
        Map<String, String> erros = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null
                                ? fieldError.getDefaultMessage()
                                : "Valor inválido.",
                        (mensagemAtual, mensagemAnterior) -> mensagemAtual,
                        LinkedHashMap::new
                ));

        ErroResponse erro = new ErroResponse(
                "Dados de entrada inválidos.",
                OffsetDateTime.now(),
                erros
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    public record ErroResponse(String mensagem, OffsetDateTime timestamp, Map<String, String> erros) {
    }
}
