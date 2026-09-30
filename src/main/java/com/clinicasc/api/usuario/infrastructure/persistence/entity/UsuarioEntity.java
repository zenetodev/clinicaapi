package com.clinicasc.api.usuario.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private com.clinicasc.api.usuario.domain.model.TipoUsuario tipo;

    protected UsuarioEntity() {
    }

    public UsuarioEntity(UUID id, String nome, String email,
                         com.clinicasc.api.usuario.domain.model.TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
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

    public com.clinicasc.api.usuario.domain.model.TipoUsuario getTipo() {
        return tipo;
    }
}