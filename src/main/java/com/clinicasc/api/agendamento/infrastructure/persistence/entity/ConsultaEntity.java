package com.clinicasc.api.agendamento.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consultas")
public class ConsultaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID pacienteId;

    @Column(name = "dentista_id", nullable = false)
    private UUID dentistaId;

    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "motivo_cancelamento", length = 255)
    private String motivoCancelamento;

    protected ConsultaEntity() {
    }

    public ConsultaEntity(UUID id, UUID pacienteId, UUID dentistaId, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, String status, String motivoCancelamento) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.dentistaId = dentistaId;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.status = status;
        this.motivoCancelamento = motivoCancelamento;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public UUID getDentistaId() {
        return dentistaId;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public String getStatus() {
        return status;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }
}
