package com.clinicasc.api.agendamento.application.dto;

import com.clinicasc.api.agendamento.domain.model.StatusConsulta;

import java.time.LocalDateTime;
import java.util.UUID;

public record ListarConsultasInput(
        UUID pacienteId,
        UUID dentistaId,
        StatusConsulta status,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim
) {
}