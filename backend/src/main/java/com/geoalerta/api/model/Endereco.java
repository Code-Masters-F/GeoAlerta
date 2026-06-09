package com.geoalerta.api.model;

/**
 * Entidade que mapeia a tabela {@code enderecos} (endereco de uma empresa,
 * representado por um Plus Code).
 */
public class Endereco {

    private Integer id;
    private String cnpj;
    private String erdPlusCode;

    public Endereco() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getErdPlusCode() {
        return erdPlusCode;
    }

    public void setErdPlusCode(String erdPlusCode) {
        this.erdPlusCode = erdPlusCode;
    }
}
