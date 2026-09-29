package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarConsultaUseCase {

    private final ConsultaRepository consultaRepository;

    public BuscarConsultaUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public Optional<ConsultaOutput> executar(UUID consultaId) {
        return consultaRepository.buscarPorId(ConsultaId.de(consultaId))
                .map(this::toOutput);
    }

    private ConsultaOutput toOutput(Consulta consulta) {
        return new ConsultaOutput(
                consulta.getId().valor(),
                consulta.getPacienteId(),
                consulta.getDentistaId(),
                consulta.getPeriodo().dataHoraInicio(),
                consulta.getPeriodo().dataHoraFim(),
                consulta.getStatus(),
                consulta.getMotivoCancelamento()
        );
    }
}