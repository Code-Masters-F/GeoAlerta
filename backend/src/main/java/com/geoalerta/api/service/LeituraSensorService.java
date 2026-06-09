package com.geoalerta.api.service;

import com.geoalerta.api.model.LeituraSensor;
import com.geoalerta.api.repository.LeituraSensorRepository;
import com.geoalerta.api.repository.SensorRepository;
import com.geoalerta.api.util.ApiException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Regras de negocio da entidade {@link LeituraSensor}.
 */
public class LeituraSensorService {

    private final LeituraSensorRepository repository = new LeituraSensorRepository();
    private final SensorRepository sensorRepository = new SensorRepository();

    public List<LeituraSensor> listar() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public List<LeituraSensor> listarPorSensor(int sensorId) {
        try {
            exigirSensorExistente(sensorId);
            return repository.findBySensor(sensorId);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public LeituraSensor buscar(int id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> ApiException.notFound("Leitura " + id + " nao encontrada"));
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public LeituraSensor criar(LeituraSensor leitura) {
        validar(leitura);
        try {
            return repository.insert(leitura);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public LeituraSensor atualizar(int id, LeituraSensor leitura) {
        validar(leitura);
        try {
            if (!repository.update(id, leitura)) {
                throw ApiException.notFound("Leitura " + id + " nao encontrada");
            }
            leitura.setId(id);
            return leitura;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public void remover(int id) {
        try {
            if (!repository.delete(id)) {
                throw ApiException.notFound("Leitura " + id + " nao encontrada");
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    private void validar(LeituraSensor l) {
        if (l == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        if (l.getSensorId() == null) {
            throw ApiException.badRequest("O campo 'sensorId' e obrigatorio");
        }
        if (l.getValor() == null) {
            throw ApiException.badRequest("O campo 'valor' e obrigatorio");
        }
        exigirTexto(l.getUnidadeMedida(), "unidadeMedida", 5);
        if (l.getDataHora() == null) {
            l.setDataHora(LocalDateTime.now());
        }
        try {
            exigirSensorExistente(l.getSensorId());
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    private void exigirSensorExistente(int sensorId) throws SQLException {
        if (sensorRepository.findById(sensorId).isEmpty()) {
            throw ApiException.badRequest("Sensor " + sensorId + " nao encontrado");
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
