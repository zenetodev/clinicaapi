package com.clinicasc.api.agendamento.infrastructure.persistence.adapter;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;
import com.clinicasc.api.agendamento.domain.model.Consulta;
import com.clinicasc.api.agendamento.domain.model.ConsultaId;
import com.clinicasc.api.agendamento.domain.model.PeriodoConsulta;
import com.clinicasc.api.agendamento.domain.model.StatusConsulta;
import com.clinicasc.api.agendamento.domain.repository.ConsultaRepository;
import com.clinicasc.api.agendamento.infrastructure.persistence.entity.ConsultaEntity;
import com.clinicasc.api.agendamento.infrastructure.persistence.repository.SpringDataConsultaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public class ConsultaRepositoryImpl implements ConsultaRepository {

    private final SpringDataConsultaRepository springDataConsultaRepository;

    public ConsultaRepositoryImpl(SpringDataConsultaRepository springDataConsultaRepository) {
        this.springDataConsultaRepository = springDataConsultaRepository;
    }

    @Override
    public Consulta salvar(Consulta consulta) {
        ConsultaEntity entity = toEntity(consulta);
        ConsultaEntity saved = springDataConsultaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Consulta> buscarPorId(ConsultaId id) {
        return springDataConsultaRepository.findById(id.valor()).map(this::toDomain);
    }

    @Override
    public List<Consulta> listar(UUID pacienteId, UUID dentistaId, String status,
                                 LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        return springDataConsultaRepository.listar(
                        pacienteId,
                        dentistaId,
                        status,
                        dataHoraInicio,
                        dataHoraFim
                )
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existeConflitoDeHorario(UUID dentistaId, PeriodoConsulta periodo) {
        return springDataConsultaRepository.existeConflitoDeHorario(
                dentistaId,
                periodo.dataHoraInicio(),
                periodo.dataHoraFim(),
                StatusConsulta.CANCELADA.name()
        );
    }

    private ConsultaEntity toEntity(Consulta consulta) {
        return new ConsultaEntity(
                consulta.getId().valor(),
                consulta.getPacienteId(),
                consulta.getDentistaId(),
                consulta.getPeriodo().dataHoraInicio(),
                consulta.getPeriodo().dataHoraFim(),
                consulta.getStatus().name(),
                consulta.getMotivoCancelamento()
        );
    }

    private Consulta toDomain(ConsultaEntity entity) {
        return new Consulta(
                ConsultaId.de(entity.getId()),
                entity.getPacienteId(),
                entity.getDentistaId(),
                new PeriodoConsulta(entity.getDataHoraInicio(), entity.getDataHoraFim()),
                toStatus(entity.getStatus()),
                entity.getMotivoCancelamento()
        );
    }

    private StatusConsulta toStatus(String status) {
        try {
            return StatusConsulta.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new RegraDeNegocioException("Status de consulta inválido persistido: " + status);
        }
    }
}
