package com.clinicasc.api.agendamento.application.usecase;

import com.clinicasc.api.agendamento.application.dto.ConsultaOutput;
import com.clinicasc.api.agendamento.application.dto.UsuarioAutenticado;
import com.clinicasc.api.agendamento.domain.exception.AcessoNegadoException;
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

    public Optional<ConsultaOutput> executar(UUID consultaId, UsuarioAutenticado usuario) {
        return consultaRepository.buscarPorId(ConsultaId.de(consultaId))
                .map(consulta -> validarAcesso(consulta, usuario))
                .map(this::toOutput);
    }

    private Consulta validarAcesso(Consulta consulta, UsuarioAutenticado usuario) {
        boolean pacienteAutorizado = usuario.tipo() == com.clinicasc.api.usuario.domain.model.TipoUsuario.PACIENTE
                && usuario.id().equals(consulta.getPacienteId());
        boolean dentistaAutorizado = usuario.tipo() == com.clinicasc.api.usuario.domain.model.TipoUsuario.DENTISTA
                && usuario.id().equals(consulta.getDentistaId());
        if (!pacienteAutorizado && !dentistaAutorizado) {
            throw new AcessoNegadoException("O usuário autenticado não pode consultar este agendamento.");
        }
        return consulta;
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