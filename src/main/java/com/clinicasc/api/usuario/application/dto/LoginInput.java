package com.clinicasc.api.usuario.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginInput(
        @NotBlank @Email String email,
        @NotBlank String senha
) {
}