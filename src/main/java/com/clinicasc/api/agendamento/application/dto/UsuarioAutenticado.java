package com.clinicasc.api.agendamento.application.dto;

import com.clinicasc.api.usuario.domain.model.TipoUsuario;

import java.util.UUID;

public record UsuarioAutenticado(UUID id, TipoUsuario tipo) {
}