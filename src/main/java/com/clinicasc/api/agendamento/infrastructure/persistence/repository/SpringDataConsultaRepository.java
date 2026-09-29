package com.clinicasc.api.agendamento.infrastructure.persistence.repository;

import com.clinicasc.api.agendamento.infrastructure.persistence.entity.ConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SpringDataConsultaRepository extends JpaRepository<ConsultaEntity, UUID> {

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
