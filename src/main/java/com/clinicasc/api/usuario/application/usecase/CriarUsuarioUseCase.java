package com.clinicasc.api.usuario.application.usecase;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.usuario.application.dto.CriarUsuarioInput;
import com.clinicasc.api.usuario.application.dto.UsuarioOutput;
import com.clinicasc.api.usuario.domain.model.Usuario;
import com.clinicasc.api.usuario.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CriarUsuarioUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioOutput executar(CriarUsuarioInput input) {
        String email = input.email().trim().toLowerCase();
        if (usuarioRepository.existePorEmail(email)) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com o e-mail informado.");
        }

        Usuario usuario = new Usuario(input.nome(), email, passwordEncoder.encode(input.senha()), input.tipo());
        Usuario salvo = usuarioRepository.salvar(usuario);
        return new UsuarioOutput(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getTipo());
    }
}