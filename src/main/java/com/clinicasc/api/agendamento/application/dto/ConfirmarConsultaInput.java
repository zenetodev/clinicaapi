package com.clinicasc.api.agendamento.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConfirmarConsultaInput(
        @NotNull UUID consultaId
) {
}