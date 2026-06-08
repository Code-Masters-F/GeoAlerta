CREATE TABLE EmpresaAgricola (
    CNPJ          CHAR(14)     NOT NULL,
    NomeFantasia  VARCHAR(60)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    senha_hash    VARCHAR(255) NOT NULL,
    CONSTRAINT pk_empresa_agricola PRIMARY KEY (CNPJ),
    CONSTRAINT ck_empresa_cnpj_formato CHECK (CNPJ ~ '^[0-9]{14}$')
);

CREATE UNIQUE INDEX uq_empresa_agricola_email_ci
    ON EmpresaAgricola (LOWER(email));

CREATE TABLE Alertas (
    ID              INT          GENERATED ALWAYS AS IDENTITY,
    Nome            VARCHAR(60)  NOT NULL,
    Tipo            VARCHAR(30)  NOT NULL,
    Grau_Gravidade  VARCHAR(20)  NOT NULL,
    Data_de_Emissao TIMESTAMP    NOT NULL,
    Descricao       TEXT,
    CONSTRAINT pk_alertas PRIMARY KEY (ID)
);

CREATE TABLE SensorIOT (
    ID                INT          GENERATED ALWAYS AS IDENTITY,
    Tipo              VARCHAR(30)  NOT NULL,
    ERD_PlusCode      VARCHAR(11)  NOT NULL,
    Status            VARCHAR(15)  NOT NULL,
    Data_Instalacao   TIMESTAMP    NOT NULL,
    CONSTRAINT pk_sensor_iot PRIMARY KEY (ID),
    CONSTRAINT ck_sensor_iot_status CHECK (Status IN ('ATIVO', 'SUSPENSO', 'DESATIVADO'))
);

CREATE TABLE LeituraSensor (
    ID               INT           GENERATED ALWAYS AS IDENTITY,
    Sensor_ID        INT           NOT NULL,
    Valor            DECIMAL(10,5) NOT NULL,
    Unidade_Medida   VARCHAR(5)    NOT NULL,
    Data_Hora        TIMESTAMP     NOT NULL,
    CONSTRAINT pk_leitura_sensor PRIMARY KEY (ID),
    CONSTRAINT fk_leitura_sensor_sensor FOREIGN KEY (Sensor_ID)
        REFERENCES SensorIOT (ID)
);

CREATE TABLE Enderecos (
    ID            INT         GENERATED ALWAYS AS IDENTITY,
    CNPJ          CHAR(14)    NOT NULL,
    ERD_PlusCode  VARCHAR(11) NOT NULL,
    CONSTRAINT pk_enderecos PRIMARY KEY (ID),
    CONSTRAINT fk_enderecos_empresa FOREIGN KEY (CNPJ)
        REFERENCES EmpresaAgricola (CNPJ)
);

CREATE TABLE RegioesAfetadas (
    ID            INT         GENERATED ALWAYS AS IDENTITY,
    Alerta_ID     INT         NOT NULL,
    ERD_PlusCode  VARCHAR(11) NOT NULL,
    CONSTRAINT pk_regioes_afetadas PRIMARY KEY (ID),
    CONSTRAINT fk_regioes_alerta FOREIGN KEY (Alerta_ID)
        REFERENCES Alertas (ID)
);

CREATE TABLE SensoresAlerta (
    Alerta_ID   INT NOT NULL,
    Leitura_ID  INT NOT NULL,
    CONSTRAINT pk_sensores_alerta PRIMARY KEY (Alerta_ID, Leitura_ID),
    CONSTRAINT fk_sensores_alerta_alerta FOREIGN KEY (Alerta_ID)
        REFERENCES Alertas (ID),
    CONSTRAINT fk_sensores_alerta_leitura FOREIGN KEY (Leitura_ID)
        REFERENCES LeituraSensor (ID)
);

CREATE TABLE NotificacoesRecebidas (
    CNPJ       CHAR(14)    NOT NULL,
    Alerta_ID  INT         NOT NULL,
    CONSTRAINT pk_notificacoes_recebidas PRIMARY KEY (CNPJ, Alerta_ID),
    CONSTRAINT fk_notificacoes_empresa FOREIGN KEY (CNPJ)
        REFERENCES EmpresaAgricola (CNPJ),
    CONSTRAINT fk_notificacoes_alerta FOREIGN KEY (Alerta_ID)
        REFERENCES Alertas (ID)
);
