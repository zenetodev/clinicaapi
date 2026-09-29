package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.CancelarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelarConsultaUseCase {

    private final ConsultaRepository consultaRepository;

    public CancelarConsultaUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public ConsultaOutput executar(CancelarConsultaInput input) {
        Consulta consulta = consultaRepository.buscarPorId(ConsultaId.de(input.consultaId()))
                .orElseThrow(() -> new RegraDeNegocioException("Consulta não encontrada para o identificador informado."));

        consulta.cancelar(input.motivo());
        Consulta consultaAtualizada = consultaRepository.salvar(consulta);
        return toOutput(consultaAtualizada);
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
