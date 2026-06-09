package com.geoalerta.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que mapeia a tabela {@code leiturasensor}.
 */
public class LeituraSensor {

    private Integer id;
    private Integer sensorId;
    private BigDecimal valor;
    private String unidadeMedida;
    private LocalDateTime dataHora;

    public LeituraSensor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
