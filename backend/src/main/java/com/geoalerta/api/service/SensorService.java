package com.geoalerta.api.service;

import com.geoalerta.api.model.SensorIOT;
import com.geoalerta.api.repository.SensorRepository;
import com.geoalerta.api.util.ApiException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Regras de negocio da entidade {@link SensorIOT}.
 */
public class SensorService {

    /** Status validos conforme a constraint {@code ck_sensor_iot_status}. */
    private static final Set<String> STATUS_VALIDOS = Set.of("ATIVO", "SUSPENSO", "DESATIVADO");

    private final SensorRepository repository = new SensorRepository();

    public List<SensorIOT> listar() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public SensorIOT buscar(int id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> ApiException.notFound("Sensor " + id + " nao encontrado"));
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public SensorIOT criar(SensorIOT sensor) {
        validar(sensor);
        try {
            return repository.insert(sensor);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public SensorIOT atualizar(int id, SensorIOT sensor) {
        validar(sensor);
        try {
            if (!repository.update(id, sensor)) {
                throw ApiException.notFound("Sensor " + id + " nao encontrado");
            }
            sensor.setId(id);
            return sensor;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public void remover(int id) {
        try {
            if (!repository.delete(id)) {
                throw ApiException.notFound("Sensor " + id + " nao encontrado");
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    private void validar(SensorIOT s) {
        if (s == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        exigirTexto(s.getTipo(), "tipo", 30);
        exigirTexto(s.getErdPlusCode(), "erdPlusCode", 11);

        if (s.getStatus() == null || s.getStatus().isBlank()) {
            throw ApiException.badRequest("O campo 'status' e obrigatorio");
        }
        s.setStatus(s.getStatus().toUpperCase());
        if (!STATUS_VALIDOS.contains(s.getStatus())) {
            throw ApiException.badRequest("status deve ser um de " + STATUS_VALIDOS);
        }
        if (s.getDataInstalacao() == null) {
            s.setDataInstalacao(LocalDateTime.now());
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
