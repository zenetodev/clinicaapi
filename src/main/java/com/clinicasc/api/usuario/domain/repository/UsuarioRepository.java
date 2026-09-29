package com.clinicasc.api.usuario.domain.repository;

import com.clinicasc.api.usuario.domain.model.Usuario;

public interface UsuarioRepository {

    boolean existePorEmail(String email);

    Usuario salvar(Usuario usuario);
}