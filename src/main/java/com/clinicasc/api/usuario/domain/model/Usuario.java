package com.clinicasc.api.usuario.domain.model;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;

import java.util.Locale;
import java.util.UUID;

public class Usuario {

    private final UUID id;
    private final String nome;
    private final String email;
    private final TipoUsuario tipo;

    public Usuario(String nome, String email, TipoUsuario tipo) {
        this.id = UUID.randomUUID();
        this.nome = normalizarNome(nome);
        this.email = normalizarEmail(email);
        if (tipo == null) {
            throw new RegraDeNegocioException("O tipo do usuário é obrigatório.");
        }
        this.tipo = tipo;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    private String normalizarNome(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraDeNegocioException("O nome do usuário é obrigatório.");
        }
        return valor.trim();
    }

    private String normalizarEmail(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraDeNegocioException("O e-mail do usuário é obrigatório.");
        }
        return valor.trim().toLowerCase(Locale.ROOT);
    }
}