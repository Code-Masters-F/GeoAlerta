package com.geoalerta.api.model;

/**
 * Entidade que mapeia a tabela {@code regioesafetadas} (regiao atingida por um
 * alerta, representada por um Plus Code).
 */
public class RegiaoAfetada {

    private Integer id;
    private Integer alertaId;
    private String erdPlusCode;

    public RegiaoAfetada() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlertaId() {
        return alertaId;
    }

    public void setAlertaId(Integer alertaId) {
        this.alertaId = alertaId;
    }

    public String getErdPlusCode() {
        return erdPlusCode;
    }

    public void setErdPlusCode(String erdPlusCode) {
        this.erdPlusCode = erdPlusCode;
    }
}
