package com.geoalerta.api.model;

/**
 * Objeto de entrada (request body) para criacao/atualizacao de empresas.
 * Diferente da entidade {@link EmpresaAgricola}, carrega a senha em texto
 * puro, que e convertida em hash pela camada Service antes de persistir.
 */
public class EmpresaInput {

    private String cnpj;
    private String nomeFantasia;
    private String email;
    private String senha;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
