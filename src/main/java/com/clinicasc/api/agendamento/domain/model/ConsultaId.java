package com.clinicasc.api.agendamento.domain.model;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;

import java.util.UUID;

public record ConsultaId(UUID valor) {
    public ConsultaId {
        if (valor == null) throw new RegraDeNegocioException("O identificador da consulta não pode ser nulo");
    }

    public static ConsultaId gerar() {
        return new ConsultaId(UUID.randomUUID());
    }

    public static ConsultaId de(UUID uuid) {
        return new ConsultaId(uuid);
    }
}
