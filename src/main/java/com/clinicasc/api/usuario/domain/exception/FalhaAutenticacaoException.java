package com.clinicasc.api.usuario.domain.exception;

public class FalhaAutenticacaoException extends RuntimeException {

    public FalhaAutenticacaoException() {
        super("E-mail ou senha inválidos.");
    }
}