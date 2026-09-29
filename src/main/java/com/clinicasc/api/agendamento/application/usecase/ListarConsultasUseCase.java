package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.ConsultaPageOutput;
import com.clinicasc.api.agendamento.application.dto.ListarConsultasInput;
import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaPage;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarConsultasUseCase {

    private final ConsultaRepository consultaRepository;

    public ListarConsultasUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

        public ConsultaPageOutput executar(ListarConsultasInput input) {
        validarPeriodo(input);
        validarPaginacao(input);

        ConsultaPage pagina = consultaRepository.listar(
                        input.pacienteId(),
                        input.dentistaId(),
                        input.status() != null ? input.status().name() : null,
                input.dataHoraInicio(),
                input.dataHoraFim(),
                input.pagina(),
                input.tamanho()
            );
        List<ConsultaOutput> consultas = pagina.consultas().stream()
                .map(this::toOutput)
                .toList();
        return new ConsultaPageOutput(
            consultas,
            pagina.pagina(),
            pagina.tamanho(),
            pagina.totalElementos(),
            pagina.totalPaginas()
        );
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

    private void validarPaginacao(ListarConsultasInput input) {
        if (input.pagina() < 0) {
            throw new RegraDeNegocioException("A página deve ser maior ou igual a zero.");
        }
        if (input.tamanho() < 1 || input.tamanho() > 100) {
            throw new RegraDeNegocioException("O tamanho da página deve estar entre 1 e 100.");
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