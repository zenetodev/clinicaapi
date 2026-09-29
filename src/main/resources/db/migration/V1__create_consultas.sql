CREATE TABLE IF NOT EXISTS consultas (
    id UUID NOT NULL,
    paciente_id UUID NOT NULL,
    dentista_id UUID NOT NULL,
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    motivo_cancelamento VARCHAR(255),
    CONSTRAINT pk_consultas PRIMARY KEY (id),
    CONSTRAINT ck_consultas_periodo CHECK (data_hora_fim > data_hora_inicio),
    CONSTRAINT ck_consultas_status CHECK (status IN ('AGENDADA', 'CONFIRMADA', 'REALIZADA', 'CANCELADA'))
);

CREATE INDEX IF NOT EXISTS idx_consultas_dentista_periodo
    ON consultas (dentista_id, data_hora_inicio, data_hora_fim);

CREATE INDEX IF NOT EXISTS idx_consultas_paciente
    ON consultas (paciente_id);