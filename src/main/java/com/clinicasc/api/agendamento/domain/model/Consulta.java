package com.clinicasc.api.agendamento.domain.model;

import com.clinicasc.api.agendamento.domain.exception.RegraDeNegocioException;

import java.util.UUID;


public class Consulta {

    private final ConsultaId id;
    private final UUID pacienteId;
    private final UUID dentistaId;
    private PeriodoConsulta periodo;
    private StatusConsulta status;
    private String motivoCancelamento;

    public Consulta(UUID pacienteId, UUID dentistaId, PeriodoConsulta periodo) {
        if (pacienteId == null || dentistaId == null) {
            throw new RegraDeNegocioException("Paciente e Dentista são obrigatórios para agendar uma consulta.");
        }
        this.id = ConsultaId.gerar();
        this.pacienteId = pacienteId;
        this.dentistaId = dentistaId;
        this.periodo = periodo;
        this.status = StatusConsulta.AGENDADA;
    }

    public Consulta(ConsultaId id, UUID pacienteId, UUID dentistaId, PeriodoConsulta periodo, StatusConsulta status, String motivoCancelamento) {
        if (id == null || pacienteId == null || dentistaId == null || periodo == null || status == null) {
            throw new RegraDeNegocioException("Atributos obrigatórios da consulta não podem ser nulos.");
        }
        this.id = id;
        this.pacienteId = pacienteId;
        this.dentistaId = dentistaId;
        this.periodo = periodo;
        this.status = status;
        this.motivoCancelamento = motivoCancelamento;
    }

    public void cancelar(String motivo) {
        if (this.status == StatusConsulta.REALIZADA) {
            throw new RegraDeNegocioException("Consultas já realizadas não podem ser canceladas.");
        }
        if (this.status == StatusConsulta.CANCELADA) {
            throw new RegraDeNegocioException("A consulta já está cancelada.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new RegraDeNegocioException("É necessário informar o motivo do cancelamento.");
        }
        this.status = StatusConsulta.CANCELADA;
        this.motivoCancelamento = motivo;
    }

    public void confirmar() {
        if (this.status != StatusConsulta.AGENDADA) {
            throw new RegraDeNegocioException("Apenas consultas com status AGENDADA podem ser confirmadas.");
        }
        this.status = StatusConsulta.CONFIRMADA;
    }

    public ConsultaId getId() {
        return id;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public UUID getDentistaId() {
        return dentistaId;
    }

    public PeriodoConsulta getPeriodo() {
        return periodo;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }
}