package com.clinicasc.api.agendamento.domain.repository;

import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.model.PeriodoConsulta;

import java.util.Optional;
import java.util.UUID;

public interface ConsultaRepository {

    Consulta salvar(Consulta consulta);

    Optional<Consulta> buscarPorId(ConsultaId id);

    boolean existeConflitoDeHorario(UUID dentistaId, PeriodoConsulta periodo);
}
