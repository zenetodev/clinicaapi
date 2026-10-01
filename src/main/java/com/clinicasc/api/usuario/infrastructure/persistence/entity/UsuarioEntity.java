package com.clinicasc.api.usuario.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.UUID;

import com.clinicasc.api.usuario.domain.model.TipoUsuario;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 254)
    @Email
    private String email;

    @Column(name = "senha_hash", length = 100)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.clinicasc.api.usuario.domain.model.TipoUsuario tipo;

    protected UsuarioEntity() {
    }

    public UsuarioEntity(UUID id, String nome, String email, String senhaHash,
                         com.clinicasc.api.usuario.domain.model.TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
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

    public String getSenhaHash() {
        return senhaHash;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }
}