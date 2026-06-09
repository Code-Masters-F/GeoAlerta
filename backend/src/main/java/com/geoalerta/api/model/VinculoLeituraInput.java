package com.geoalerta.api.model;

import java.util.List;

/**
 * Corpo da requisicao para vincular leituras a um alerta
 * (tabela de juncao {@code sensoresalerta}).
 *
 * <p>Aceita um unico id ({@code leituraId}) ou uma lista ({@code leituraIds}).
 */
public class VinculoLeituraInput {

    private Integer leituraId;
    private List<Integer> leituraIds;

    public VinculoLeituraInput() {
    }

    public Integer getLeituraId() {
        return leituraId;
    }

    public void setLeituraId(Integer leituraId) {
        this.leituraId = leituraId;
    }

    public List<Integer> getLeituraIds() {
        return leituraIds;
    }

    public void setLeituraIds(List<Integer> leituraIds) {
        this.leituraIds = leituraIds;
    }
}
