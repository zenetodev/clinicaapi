CREATE TABLE IF NOT EXISTS usuarios (
    id UUID NOT NULL,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(254) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT ck_usuarios_tipo CHECK (tipo IN ('PACIENTE', 'DENTISTA'))
);