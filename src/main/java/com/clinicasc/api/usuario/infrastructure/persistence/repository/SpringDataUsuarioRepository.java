package com.clinicasc.api.usuario.infrastructure.persistence.repository;

import com.clinicasc.api.usuario.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {

    boolean existsByEmailIgnoreCase(String email);
}