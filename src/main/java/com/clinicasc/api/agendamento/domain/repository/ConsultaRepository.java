package com.clinicasc.api.agendamento.domain.repository;

import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaPage;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.model.PeriodoConsulta;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface ConsultaRepository {

    Consulta salvar(Consulta consulta);

    Optional<Consulta> buscarPorId(ConsultaId id);

    ConsultaPage listar(UUID pacienteId, UUID dentistaId, String status,
                        LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
                        int pagina, int tamanho);

    boolean existeConflitoDeHorario(UUID dentistaId, PeriodoConsulta periodo);
}
