package com.clinicasc.api.agendamento.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgendarConsultaInput(
        @NotNull UUID pacienteId,
        @NotNull UUID dentistaId,
        @NotNull LocalDateTime dataHoraInicio,
        @NotNull LocalDateTime dataHoraFim
) {
}
