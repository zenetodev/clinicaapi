package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConfirmarConsultaInput;
import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.UsuarioAutenticado;
import com.clinicasc.api.agendamento.domain.exception.AcessoNegadoException;
import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmarConsultaUseCase {

    private final ConsultaRepository consultaRepository;

    public ConfirmarConsultaUseCase(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public ConsultaOutput executar(ConfirmarConsultaInput input, UsuarioAutenticado usuario) {
        Consulta consulta = consultaRepository.buscarPorId(ConsultaId.de(input.consultaId()))
                .orElseThrow(() -> new RegraDeNegocioException("Consulta não encontrada para o identificador informado."));

            if (usuario.tipo() != com.clinicasc.api.usuario.domain.model.TipoUsuario.DENTISTA
                || !usuario.id().equals(consulta.getDentistaId())) {
                throw new AcessoNegadoException("Apenas o dentista responsável pode confirmar esta consulta.");
            }

        consulta.confirmar();
        Consulta consultaAtualizada = consultaRepository.salvar(consulta);
        return new ConsultaOutput(
                consultaAtualizada.getId().valor(),
                consultaAtualizada.getPacienteId(),
                consultaAtualizada.getDentistaId(),
                consultaAtualizada.getPeriodo().dataHoraInicio(),
                consultaAtualizada.getPeriodo().dataHoraFim(),
                consultaAtualizada.getStatus(),
                consultaAtualizada.getMotivoCancelamento()
        );
    }
}