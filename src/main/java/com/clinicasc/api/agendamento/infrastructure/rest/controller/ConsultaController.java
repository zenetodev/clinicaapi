package com.clinicasc.api.agendamento.infrastructure.rest.controller;

import com.clinicasc.api.agendamento.application.dto.AgendarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.CancelarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConfirmarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.usecase.AgendarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.BuscarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.CancelarConsultaUseCase;
import com.clinicasc.api.agendamento.application.usecase.ConfirmarConsultaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/agendamentos")
public class ConsultaController {

    private final AgendarConsultaUseCase agendarConsultaUseCase;
    private final BuscarConsultaUseCase buscarConsultaUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final ConfirmarConsultaUseCase confirmarConsultaUseCase;

    public ConsultaController(AgendarConsultaUseCase agendarConsultaUseCase,
                              BuscarConsultaUseCase buscarConsultaUseCase,
                              CancelarConsultaUseCase cancelarConsultaUseCase,
                              ConfirmarConsultaUseCase confirmarConsultaUseCase) {
        this.agendarConsultaUseCase = agendarConsultaUseCase;
        this.buscarConsultaUseCase = buscarConsultaUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.confirmarConsultaUseCase = confirmarConsultaUseCase;
    }

    @PostMapping
    public ResponseEntity<ConsultaOutput> agendar(@Valid @RequestBody AgendarConsultaInput input) {
        ConsultaOutput output = agendarConsultaUseCase.executar(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("/{consultaId}")
    public ResponseEntity<ConsultaOutput> buscarPorId(@PathVariable UUID consultaId) {
        return buscarConsultaUseCase.executar(consultaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cancelar")
    public ResponseEntity<ConsultaOutput> cancelar(@Valid @RequestBody CancelarConsultaInput input) {
        ConsultaOutput output = cancelarConsultaUseCase.executar(input);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<ConsultaOutput> confirmar(@Valid @RequestBody ConfirmarConsultaInput input) {
        ConsultaOutput output = confirmarConsultaUseCase.executar(input);
        return ResponseEntity.ok(output);
    }
}
