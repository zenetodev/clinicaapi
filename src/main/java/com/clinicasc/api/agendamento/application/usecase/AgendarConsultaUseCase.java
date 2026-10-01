package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.AgendarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.UsuarioAutenticado;
import com.clinicasc.api.agendamento.domain.exception.AcessoNegadoException;
import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.PeriodoConsulta;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendarConsultaUseCase {

    private final ConsultaRepository consultaRepository;

    public AgendarConsultaUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public ConsultaOutput executar(AgendarConsultaInput input, UsuarioAutenticado usuario) {
        if (usuario.tipo() != com.clinicasc.api.usuario.domain.model.TipoUsuario.PACIENTE
                || !usuario.id().equals(input.pacienteId())) {
            throw new AcessoNegadoException("O paciente autenticado só pode agendar consultas para si mesmo.");
        }
        PeriodoConsulta periodo = new PeriodoConsulta(input.dataHoraInicio(), input.dataHoraFim());

        if (consultaRepository.existeConflitoDeHorario(input.dentistaId(), periodo)) {
            throw new RegraDeNegocioException("Já existe uma consulta para o dentista no período informado.");
        }

        Consulta consulta = new Consulta(input.pacienteId(), input.dentistaId(), periodo);
        Consulta consultaSalva = consultaRepository.salvar(consulta);
        return toOutput(consultaSalva);
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
