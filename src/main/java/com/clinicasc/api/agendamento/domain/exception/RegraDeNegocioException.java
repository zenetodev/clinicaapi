package com.clinicasc.api.agendamento.domain.exception;

public class RegraDeNegocioException extends RuntimeException{
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
