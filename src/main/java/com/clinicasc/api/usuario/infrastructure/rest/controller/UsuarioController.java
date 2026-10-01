package com.clinicasc.api.usuario.infrastructure.rest.controller;

import com.clinicasc.api.usuario.application.dto.CriarUsuarioInput;
import com.clinicasc.api.usuario.application.dto.LoginInput;
import com.clinicasc.api.usuario.application.dto.LoginOutput;
import com.clinicasc.api.usuario.application.dto.UsuarioOutput;
import com.clinicasc.api.usuario.application.usecase.AutenticarUsuarioUseCase;
import com.clinicasc.api.usuario.application.usecase.CriarUsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase,
                             AutenticarUsuarioUseCase autenticarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioOutput> criar(@Valid @RequestBody CriarUsuarioInput input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarUsuarioUseCase.executar(input));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(@Valid @RequestBody LoginInput input) {
        return ResponseEntity.ok(autenticarUsuarioUseCase.executar(input));
    }
}