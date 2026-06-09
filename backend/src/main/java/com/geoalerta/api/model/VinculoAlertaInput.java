package com.geoalerta.api.model;

import java.util.List;

/**
 * Corpo da requisicao para registrar notificacoes de alertas recebidas por uma
 * empresa (tabela de juncao {@code notificacoesrecebidas}).
 *
 * <p>Aceita um unico id ({@code alertaId}) ou uma lista ({@code alertaIds}).
 */
public class VinculoAlertaInput {

    private Integer alertaId;
    private List<Integer> alertaIds;

    public VinculoAlertaInput() {
    }

    public Integer getAlertaId() {
        return alertaId;
    }

    public void setAlertaId(Integer alertaId) {
        this.alertaId = alertaId;
    }

    public List<Integer> getAlertaIds() {
        return alertaIds;
    }

    public void setAlertaIds(List<Integer> alertaIds) {
        this.alertaIds = alertaIds;
    }
}
