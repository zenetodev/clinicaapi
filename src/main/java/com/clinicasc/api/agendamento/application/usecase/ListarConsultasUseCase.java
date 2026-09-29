package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.ListarConsultasInput;
import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarConsultasUseCase {

    private final ConsultaRepository consultaRepository;

    public ListarConsultasUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<ConsultaOutput> executar(ListarConsultasInput input) {
        validarPeriodo(input);

        return consultaRepository.listar(
                        input.pacienteId(),
                        input.dentistaId(),
                        input.status() != null ? input.status().name() : null,
                input.dataHoraInicio(),
                input.dataHoraFim()
                )
                .stream()
                .map(this::toOutput)
                .toList();
    }

    private void validarPeriodo(ListarConsultasInput input) {
        if (input.dataHoraInicio() == null && input.dataHoraFim() == null) {
            return;
        }
        if (input.dataHoraInicio() == null || input.dataHoraFim() == null) {
            throw new RegraDeNegocioException("Informe data/hora inicial e final para filtrar por período.");
        }
        if (!input.dataHoraFim().isAfter(input.dataHoraInicio())) {
            throw new RegraDeNegocioException("A data/hora final do filtro deve ser posterior à inicial.");
        }
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