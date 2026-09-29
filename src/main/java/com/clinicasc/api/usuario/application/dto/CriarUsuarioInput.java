package com.clinicasc.api.usuario.application.dto;

import com.clinicasc.api.usuario.domain.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioInput(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotNull TipoUsuario tipo
) {
}