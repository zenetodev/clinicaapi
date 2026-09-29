package com.clinicasc.api.agendamento.application.dto;

import com.clinicasc.api.agendamento.domain.model.StatusConsulta;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaOutput(
        UUID id,
        UUID pacienteId,
        UUID dentistaId,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        StatusConsulta status,
        String motivoCancelamento
) {
}
