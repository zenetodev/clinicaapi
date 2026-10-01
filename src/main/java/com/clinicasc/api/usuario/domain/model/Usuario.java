package com.clinicasc.api.usuario.domain.model;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;

import java.util.Locale;
import java.util.UUID;

public class Usuario {

    private final UUID id;
    private final String nome;
    private final String email;
    private final String senhaHash;
    private final TipoUsuario tipo;

    public Usuario(String nome, String email, String senhaHash, TipoUsuario tipo) {
        this(UUID.randomUUID(), nome, email, senhaHash, tipo);
    }

    private Usuario(UUID id, String nome, String email, String senhaHash, TipoUsuario tipo) {
        if (id == null) {
            throw new RegraDeNegocioException("O identificador do usuário é obrigatório.");
        }
        this.id = id;
        this.nome = normalizarNome(nome);
        this.email = normalizarEmail(email);
        this.senhaHash = normalizarSenhaHash(senhaHash);
        if (tipo == null) {
            throw new RegraDeNegocioException("O tipo do usuário é obrigatório.");
        }
        this.tipo = tipo;
    }

    public static Usuario reidratar(UUID id, String nome, String email, String senhaHash, TipoUsuario tipo) {
        return new Usuario(id, nome, email, senhaHash, tipo);
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

    public String getSenhaHash() {
        return senhaHash;
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

    private String normalizarSenhaHash(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RegraDeNegocioException("O hash da senha do usuário é obrigatório.");
        }
        return valor;
    }
}