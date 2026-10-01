package com.clinicasc.api.usuario.infrastructure.persistence.repository;

import com.clinicasc.api.usuario.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<UsuarioEntity> findByEmailIgnoreCase(String email);
}