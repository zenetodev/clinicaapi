package com.clinicasc.api.usuario.application.dto;

import com.clinicasc.api.usuario.domain.model.TipoUsuario;

import java.util.UUID;

public record UsuarioOutput(UUID id, String nome, String email, TipoUsuario tipo) {
}