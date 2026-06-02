CREATE TABLE EmpresaAgricola (
    CNPJ          VARCHAR(14)  NOT NULL,
    NomeFantasia  VARCHAR(60)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    senha_hash    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_empresa_agricola PRIMARY KEY (CNPJ),
    CONSTRAINT uq_empresa_email    UNIQUE (email)
);

CREATE TABLE Alertas (
    ID              INT          GENERATED ALWAYS AS IDENTITY,
    Nome            VARCHAR(60)  NOT NULL,
    Tipo            VARCHAR(30)  NOT NULL,
    Grau_Gravidade  VARCHAR(20)  NOT NULL,
    Data_de_Emissao TIMESTAMP    NOT NULL,
    Descricao       TEXT,
    CONSTRAINT pk_alertas PRIMARY KEY (ID)
);

CREATE TABLE Enderecos (
    ID            INT         GENERATED ALWAYS AS IDENTITY,
    CNPJ          VARCHAR(14) NOT NULL,
    ERD_PlusCode  VARCHAR(11) NOT NULL,
    CONSTRAINT pk_enderecos PRIMARY KEY (ID),
    CONSTRAINT fk_enderecos_empresa FOREIGN KEY (CNPJ)
        REFERENCES EmpresaAgricola (CNPJ)
);

CREATE TABLE RegioesAfetadas (
    ID            INT         GENERATED ALWAYS AS IDENTITY,
    Alertas_ID    INT         NOT NULL,
    ERD_PlusCode  VARCHAR(11) NOT NULL,
    CONSTRAINT pk_regioes_afetadas PRIMARY KEY (ID),
    CONSTRAINT fk_regioes_alerta FOREIGN KEY (Alertas_ID)
        REFERENCES Alertas (ID)
);

CREATE TABLE NotificacoesRecebidas (
    CNPJ       VARCHAR(14) NOT NULL,
    Alertas_ID INT         NOT NULL,
    CONSTRAINT pk_notificacoes_recebidas PRIMARY KEY (CNPJ, Alertas_ID),
    CONSTRAINT fk_notificacoes_empresa FOREIGN KEY (CNPJ)
        REFERENCES EmpresaAgricola (CNPJ),
    CONSTRAINT fk_notificacoes_alerta FOREIGN KEY (Alertas_ID)
        REFERENCES Alertas (ID)
);
