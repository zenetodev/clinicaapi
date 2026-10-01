package com.clinicasc.api.usuario.domain.repository;

import com.clinicasc.api.usuario.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    boolean existePorEmail(String email);

    Optional<Usuario> buscarPorEmail(String email);

    Usuario salvar(Usuario usuario);
}