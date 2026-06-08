package com.geoalerta.api.model;

import java.time.LocalDateTime;

/**
 * Entidade que mapeia a tabela {@code sensoriot}.
 */
public class SensorIOT {

    private Integer id;
    private String tipo;
    private String erdPlusCode;
    private String status;
    private LocalDateTime dataInstalacao;

    public SensorIOT() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getErdPlusCode() {
        return erdPlusCode;
    }

    public void setErdPlusCode(String erdPlusCode) {
        this.erdPlusCode = erdPlusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataInstalacao() {
        return dataInstalacao;
    }

    public void setDataInstalacao(LocalDateTime dataInstalacao) {
        this.dataInstalacao = dataInstalacao;
    }
}
