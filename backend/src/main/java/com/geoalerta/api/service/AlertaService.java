package com.geoalerta.api.service;

import com.geoalerta.api.model.Alerta;
import com.geoalerta.api.model.LeituraSensor;
import com.geoalerta.api.model.RegiaoAfetada;
import com.geoalerta.api.repository.AlertaRepository;
import com.geoalerta.api.repository.LeituraSensorRepository;
import com.geoalerta.api.repository.RegiaoAfetadaRepository;
import com.geoalerta.api.repository.SensorAlertaRepository;
import com.geoalerta.api.util.ApiException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Regras de negocio e validacoes da entidade {@link Alerta}. Converte
 * {@link SQLException} (camada Repository) em {@link ApiException} (HTTP 500).
 */
public class AlertaService {

    private final AlertaRepository repository = new AlertaRepository();
    private final SensorAlertaRepository sensorAlertaRepository = new SensorAlertaRepository();
    private final LeituraSensorRepository leituraRepository = new LeituraSensorRepository();
    private final RegiaoAfetadaRepository regiaoRepository = new RegiaoAfetadaRepository();

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
        validarCampos(alerta);
        try {
            if (alerta.getDataDeEmissao() == null) {
                // Cliente nao enviou a data: preserva a data de emissao original
                // em vez de sobrescreve-la com "agora".
                Alerta existente = repository.findById(id)
                        .orElseThrow(() -> ApiException.notFound("Alerta " + id + " nao encontrado"));
                alerta.setDataDeEmissao(existente.getDataDeEmissao());
            }
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

    // ----- Leituras que originaram o alerta (tabela de juncao sensoresalerta) -----

    /** Lista as leituras vinculadas ao alerta. */
    public List<LeituraSensor> listarLeituras(int alertaId) {
        exigirAlerta(alertaId);
        try {
            return sensorAlertaRepository.findLeiturasByAlerta(alertaId);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /**
     * Vincula uma ou mais leituras ao alerta e devolve as leituras vinculadas.
     * Ids ja vinculados sao ignorados (operacao idempotente).
     */
    public List<LeituraSensor> vincularLeituras(int alertaId, List<Integer> leituraIds) {
        exigirAlerta(alertaId);
        if (leituraIds == null || leituraIds.isEmpty()) {
            throw ApiException.badRequest("Informe 'leituraId' ou 'leituraIds'");
        }
        List<LeituraSensor> vinculadas = new ArrayList<>();
        try {
            for (Integer leituraId : leituraIds) {
                if (leituraId == null) {
                    throw ApiException.badRequest("Lista de leituras contem id nulo");
                }
                LeituraSensor leitura = leituraRepository.findById(leituraId)
                        .orElseThrow(() -> ApiException.badRequest(
                                "Leitura " + leituraId + " nao encontrada"));
                if (!sensorAlertaRepository.exists(alertaId, leituraId)) {
                    sensorAlertaRepository.link(alertaId, leituraId);
                }
                vinculadas.add(leitura);
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
        return vinculadas;
    }

    /** Remove o vinculo entre o alerta e a leitura. */
    public void desvincularLeitura(int alertaId, int leituraId) {
        exigirAlerta(alertaId);
        try {
            if (!sensorAlertaRepository.unlink(alertaId, leituraId)) {
                throw ApiException.notFound(
                        "Leitura " + leituraId + " nao vinculada ao alerta " + alertaId);
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    // ----- Regioes afetadas pelo alerta (tabela regioesafetadas) -----

    /** Lista as regioes afetadas pelo alerta. */
    public List<RegiaoAfetada> listarRegioes(int alertaId) {
        exigirAlerta(alertaId);
        try {
            return regiaoRepository.findByAlerta(alertaId);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Busca uma regiao afetada do alerta. */
    public RegiaoAfetada buscarRegiao(int alertaId, int id) {
        exigirAlerta(alertaId);
        try {
            return regiaoDoAlerta(alertaId, id);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Adiciona uma regiao afetada ao alerta. */
    public RegiaoAfetada adicionarRegiao(int alertaId, RegiaoAfetada regiao) {
        exigirAlerta(alertaId);
        validarRegiao(regiao);
        regiao.setAlertaId(alertaId);
        try {
            return regiaoRepository.insert(regiao);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Atualiza o Plus Code de uma regiao afetada do alerta. */
    public RegiaoAfetada atualizarRegiao(int alertaId, int id, RegiaoAfetada regiao) {
        exigirAlerta(alertaId);
        validarRegiao(regiao);
        try {
            regiaoDoAlerta(alertaId, id);
            regiaoRepository.update(id, regiao);
            regiao.setId(id);
            regiao.setAlertaId(alertaId);
            return regiao;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Remove uma regiao afetada do alerta. */
    public void removerRegiao(int alertaId, int id) {
        exigirAlerta(alertaId);
        try {
            regiaoDoAlerta(alertaId, id);
            regiaoRepository.delete(id);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Garante que a regiao existe e pertence ao alerta informado. */
    private RegiaoAfetada regiaoDoAlerta(int alertaId, int id) throws SQLException {
        RegiaoAfetada regiao = regiaoRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Regiao " + id + " nao encontrada"));
        if (regiao.getAlertaId() != alertaId) {
            throw ApiException.notFound("Regiao " + id + " nao pertence ao alerta " + alertaId);
        }
        return regiao;
    }

    private void validarRegiao(RegiaoAfetada regiao) {
        if (regiao == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        exigirTexto(regiao.getErdPlusCode(), "erdPlusCode", 11);
    }

    private void exigirAlerta(int alertaId) {
        try {
            if (repository.findById(alertaId).isEmpty()) {
                throw ApiException.notFound("Alerta " + alertaId + " nao encontrado");
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Validacao para criacao: aplica os defaults (data de emissao = agora). */
    private void validar(Alerta a) {
        validarCampos(a);
        if (a.getDataDeEmissao() == null) {
            a.setDataDeEmissao(LocalDateTime.now());
        }
    }

    /** Valida apenas os campos obrigatorios, sem definir defaults. */
    private void validarCampos(Alerta a) {
        if (a == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        exigirTexto(a.getNome(), "nome", 60);
        exigirTexto(a.getTipo(), "tipo", 30);
        exigirTexto(a.getGrauGravidade(), "grauGravidade", 20);
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
