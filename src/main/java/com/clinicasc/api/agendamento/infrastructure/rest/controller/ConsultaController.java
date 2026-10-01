package com.clinicasc.api.agendamento.infrastructure.rest.controller;

import com.clinicasc.api.agendamento.application.dto.AgendarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.CancelarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConfirmarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.ConsultaPageOutput;
import com.clinicasc.api.agendamento.application.dto.ListarConsultasInput;
import com.clinicasc.api.agendamento.application.dto.UsuarioAutenticado;
import com.clinicasc.api.agendamento.application.usecase.AgendarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.BuscarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.CancelarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.ConfirmarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.ListarConsultasUseCase;
import com.clinicasc.api.agendamento.domain.model.StatusConsulta;
import com.clinicasc.api.usuario.domain.model.TipoUsuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/agendamentos")
public class ConsultaController {

    private final AgendarConsultaUseCase agendarConsultaUseCase;
    private final BuscarConsultaUseCase buscarConsultaUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final ConfirmarConsultaUseCase confirmarConsultaUseCase;
    private final ListarConsultasUseCase listarConsultasUseCase;

    public ConsultaController(AgendarConsultaUseCase agendarConsultaUseCase,
                              BuscarConsultaUseCase buscarConsultaUseCase,
                              CancelarConsultaUseCase cancelarConsultaUseCase,
                              ConfirmarConsultaUseCase confirmarConsultaUseCase,
                              ListarConsultasUseCase listarConsultasUseCase) {
        this.agendarConsultaUseCase = agendarConsultaUseCase;
        this.buscarConsultaUseCase = buscarConsultaUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.confirmarConsultaUseCase = confirmarConsultaUseCase;
        this.listarConsultasUseCase = listarConsultasUseCase;
    }

    @PostMapping
    public ResponseEntity<ConsultaOutput> agendar(@Valid @RequestBody AgendarConsultaInput input,
                                                  Authentication authentication) {
        ConsultaOutput output = agendarConsultaUseCase.executar(input, usuarioAutenticado(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("/{consultaId}")
    public ResponseEntity<ConsultaOutput> buscarPorId(@PathVariable UUID consultaId,
                                                      Authentication authentication) {
        return buscarConsultaUseCase.executar(consultaId, usuarioAutenticado(authentication))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<ConsultaPageOutput> listar(
            @RequestParam(required = false) UUID pacienteId,
            @RequestParam(required = false) UUID dentistaId,
            @RequestParam(required = false) StatusConsulta status,
            @RequestParam(required = false) LocalDateTime dataHoraInicio,
            @RequestParam(required = false) LocalDateTime dataHoraFim,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            Authentication authentication) {
        ListarConsultasInput input = new ListarConsultasInput(
                pacienteId, dentistaId, status, dataHoraInicio, dataHoraFim, pagina, tamanho
        );
        return ResponseEntity.ok(listarConsultasUseCase.executar(input, usuarioAutenticado(authentication)));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<ConsultaOutput> cancelar(@Valid @RequestBody CancelarConsultaInput input,
                                                   Authentication authentication) {
        ConsultaOutput output = cancelarConsultaUseCase.executar(input, usuarioAutenticado(authentication));
        return ResponseEntity.ok(output);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<ConsultaOutput> confirmar(@Valid @RequestBody ConfirmarConsultaInput input,
                                                    Authentication authentication) {
        ConsultaOutput output = confirmarConsultaUseCase.executar(input, usuarioAutenticado(authentication));
        return ResponseEntity.ok(output);
    }

    private UsuarioAutenticado usuarioAutenticado(Authentication authentication) {
        UUID id = UUID.fromString(authentication.getName());
        String autoridade = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow()
                .getAuthority()
                .replace("ROLE_", "");
        return new UsuarioAutenticado(id, TipoUsuario.valueOf(autoridade));
    }
}
