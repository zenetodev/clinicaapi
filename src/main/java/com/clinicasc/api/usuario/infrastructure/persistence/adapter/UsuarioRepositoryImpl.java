package com.clinicasc.api.usuario.infrastructure.persistence.adapter;

import com.clinicasc.api.usuario.domain.model.Usuario;
import com.clinicasc.api.usuario.domain.repository.UsuarioRepository;
import com.clinicasc.api.usuario.infrastructure.persistence.entity.UsuarioEntity;
import com.clinicasc.api.usuario.infrastructure.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final SpringDataUsuarioRepository springDataUsuarioRepository;

    public UsuarioRepositoryImpl(SpringDataUsuarioRepository springDataUsuarioRepository) {
        this.springDataUsuarioRepository = springDataUsuarioRepository;
    }

    @Override
    public boolean existePorEmail(String email) {
        return springDataUsuarioRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipo()
        );
        springDataUsuarioRepository.save(entity);
        return usuario;
    }
}