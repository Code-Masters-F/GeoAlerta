package com.geoalerta.api.model;

/**
 * Entidade que mapeia a tabela {@code empresaagricola}.
 *
 * <p>O hash da senha ({@code senhaHash}) e marcado como {@code transient}
 * para nunca ser serializado nas respostas JSON. A senha em texto puro
 * chega pela API atraves de {@link EmpresaInput} e jamais e persistida.
 */
public class EmpresaAgricola {

    private String cnpj;
    private String nomeFantasia;
    private String email;
    private transient String senhaHash;

    public EmpresaAgricola() {
    }

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

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
}
