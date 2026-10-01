package com.clinicasc.api.usuario.application.dto;

import com.clinicasc.api.usuario.domain.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarUsuarioInput(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 72) String senha,
        @NotNull TipoUsuario tipo
) {
}