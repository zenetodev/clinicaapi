package com.clinicasc.api.usuario.application.dto;

public record LoginOutput(String token, long expiresInSeconds) {
}