package com.clinicasc.api.agendamento.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CancelarConsultaInput(
        @NotNull UUID consultaId,
        @NotBlank String motivo
) {
}
