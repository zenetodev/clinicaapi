package com.clinicasc.api.agendamento.infrastructure.persistence.repository;

import com.clinicasc.api.agendamento.infrastructure.persistence.entity.ConsultaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SpringDataConsultaRepository extends JpaRepository<ConsultaEntity, UUID> {

    @Query("""
            select c
            from ConsultaEntity c
            where (:pacienteId is null or c.pacienteId = :pacienteId)
              and (:dentistaId is null or c.dentistaId = :dentistaId)
              and (:status is null or c.status = :status)
              and (:dataHoraInicio is null or c.dataHoraInicio >= :dataHoraInicio)
              and (:dataHoraFim is null or c.dataHoraFim <= :dataHoraFim)
            order by c.dataHoraInicio
            """)
        Page<ConsultaEntity> listar(
            @Param("pacienteId") UUID pacienteId,
            @Param("dentistaId") UUID dentistaId,
            @Param("status") String status,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim,
            Pageable pageable
    );

    @Query("""
            select (count(c) > 0)
            from ConsultaEntity c
            where c.dentistaId = :dentistaId
              and c.status <> :statusCancelada
              and c.dataHoraInicio < :fim
              and :inicio < c.dataHoraFim
            """)
    boolean existeConflitoDeHorario(
            @Param("dentistaId") UUID dentistaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCancelada") String statusCancelada
    );
}
