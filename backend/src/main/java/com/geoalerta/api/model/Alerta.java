package com.geoalerta.api.model;

import java.time.LocalDateTime;

/**
 * Entidade que mapeia a tabela {@code alertas}.
 */
public class Alerta {

    private Integer id;
    private String nome;
    private String tipo;
    private String grauGravidade;
    private LocalDateTime dataDeEmissao;
    private String descricao;

    public Alerta() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGrauGravidade() {
        return grauGravidade;
    }

    public void setGrauGravidade(String grauGravidade) {
        this.grauGravidade = grauGravidade;
    }

    public LocalDateTime getDataDeEmissao() {
        return dataDeEmissao;
    }

    public void setDataDeEmissao(LocalDateTime dataDeEmissao) {
        this.dataDeEmissao = dataDeEmissao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
