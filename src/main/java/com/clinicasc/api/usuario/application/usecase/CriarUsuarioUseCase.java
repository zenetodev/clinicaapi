package com.clinicasc.api.usuario.application.usecase;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.usuario.application.dto.CriarUsuarioInput;
import com.clinicasc.api.usuario.application.dto.UsuarioOutput;
import com.clinicasc.api.usuario.domain.model.Usuario;
import com.clinicasc.api.usuario.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public CriarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioOutput executar(CriarUsuarioInput input) {
        String email = input.email().trim().toLowerCase();
        if (usuarioRepository.existePorEmail(email)) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com o e-mail informado.");
        }

        Usuario usuario = new Usuario(input.nome(), email, input.tipo());
        Usuario salvo = usuarioRepository.salvar(usuario);
        return new UsuarioOutput(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getTipo());
    }
}