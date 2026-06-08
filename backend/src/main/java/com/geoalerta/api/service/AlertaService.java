package com.geoalerta.api.service;

import com.geoalerta.api.model.Alerta;
import com.geoalerta.api.repository.AlertaRepository;
import com.geoalerta.api.util.ApiException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Regras de negocio e validacoes da entidade {@link Alerta}. Converte
 * {@link SQLException} (camada Repository) em {@link ApiException} (HTTP 500).
 */
public class AlertaService {

    private final AlertaRepository repository = new AlertaRepository();

    public List<Alerta> listar() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public Alerta buscar(int id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> ApiException.notFound("Alerta " + id + " nao encontrado"));
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public Alerta criar(Alerta alerta) {
        validar(alerta);
        try {
            return repository.insert(alerta);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public Alerta atualizar(int id, Alerta alerta) {
        validar(alerta);
        try {
            if (!repository.update(id, alerta)) {
                throw ApiException.notFound("Alerta " + id + " nao encontrado");
            }
            alerta.setId(id);
            return alerta;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public void remover(int id) {
        try {
            if (!repository.delete(id)) {
                throw ApiException.notFound("Alerta " + id + " nao encontrado");
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    private void validar(Alerta a) {
        if (a == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        exigirTexto(a.getNome(), "nome", 60);
        exigirTexto(a.getTipo(), "tipo", 30);
        exigirTexto(a.getGrauGravidade(), "grauGravidade", 20);
        if (a.getDataDeEmissao() == null) {
            a.setDataDeEmissao(LocalDateTime.now());
        }
    }

    private void exigirTexto(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            throw ApiException.badRequest("O campo '" + campo + "' e obrigatorio");
        }
        if (valor.length() > max) {
            throw ApiException.badRequest("O campo '" + campo + "' excede " + max + " caracteres");
        }
    }

    private ApiException erroBanco(SQLException e) {
        return new ApiException(500, "Erro de banco de dados: " + e.getMessage());
    }
}
