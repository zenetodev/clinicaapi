package com.clinicasc.api.agendamento.domain.model;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;

import java.time.LocalDateTime;

public record PeriodoConsulta(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {

    public PeriodoConsulta {
        if (dataHoraInicio == null || dataHoraFim == null) {
            throw new RegraDeNegocioException("As datas de início e fim da consulta são obrigatórias.");
        }
        if (!dataHoraFim.isAfter(dataHoraInicio)) {
            throw new RegraDeNegocioException("A data/hora de término deve ser estritamente posterior à data/hora de início.");
        }
        if (dataHoraInicio.isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Não é permitido agendar consultas para datas passadas.");
        }
    }

    public boolean sobrepoe(PeriodoConsulta outro) {
        if (outro == null) {
            return false;
        }
        return this.dataHoraInicio.isBefore(outro.dataHoraFim()) && outro.dataHoraInicio().isBefore(this.dataHoraFim);
    }
}