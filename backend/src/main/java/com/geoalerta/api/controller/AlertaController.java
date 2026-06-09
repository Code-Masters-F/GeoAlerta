package com.geoalerta.api.controller;

import com.geoalerta.api.model.Alerta;
import com.geoalerta.api.model.RegiaoAfetada;
import com.geoalerta.api.model.VinculoLeituraInput;
import com.geoalerta.api.service.AlertaService;
import com.geoalerta.api.util.ApiException;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller REST de Alertas.
 *
 * <pre>
 *   GET    /alertas                       lista todos os alertas
 *   GET    /alertas/{id}                  busca um alerta
 *   POST   /alertas                       cria um alerta
 *   PUT    /alertas/{id}                  atualiza um alerta
 *   DELETE /alertas/{id}                  remove um alerta
 *
 *   GET    /alertas/{id}/leituras         lista as leituras que originaram o alerta
 *   POST   /alertas/{id}/leituras         vincula leitura(s) ao alerta
 *   DELETE /alertas/{id}/leituras/{lid}   desvincula uma leitura do alerta
 *
 *   GET    /alertas/{id}/regioes          lista as regioes afetadas pelo alerta
 *   GET    /alertas/{id}/regioes/{rid}    busca uma regiao afetada
 *   POST   /alertas/{id}/regioes          adiciona uma regiao afetada
 *   PUT    /alertas/{id}/regioes/{rid}    atualiza uma regiao afetada
 *   DELETE /alertas/{id}/regioes/{rid}    remove uma regiao afetada
 * </pre>
 */
@WebServlet(name = "AlertaController", urlPatterns = {"/alertas", "/alertas/*"})
public class AlertaController extends BaseServlet {

    private static final String LEITURAS = "leituras";
    private static final String REGIOES = "regioes";

    private final transient AlertaService service = new AlertaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] p = parts(req);
        if (p.length == 0) {
            JsonUtil.write(resp, 200, service.listar());
        } else if (p.length == 1) {
            JsonUtil.write(resp, 200, service.buscar(toInt(p[0], "id")));
        } else if (p.length == 2 && LEITURAS.equals(p[1])) {
            JsonUtil.write(resp, 200, service.listarLeituras(toInt(p[0], "id")));
        } else if (p.length == 2 && REGIOES.equals(p[1])) {
            JsonUtil.write(resp, 200, service.listarRegioes(toInt(p[0], "id")));
        } else if (p.length == 3 && REGIOES.equals(p[1])) {
            JsonUtil.write(resp, 200, service.buscarRegiao(toInt(p[0], "id"), toInt(p[2], "regiaoId")));
        } else {
            throw ApiException.badRequest("Rota nao suportada: " + req.getPathInfo());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] p = parts(req);
        if (p.length == 0) {
            Alerta criado = service.criar(body(req, Alerta.class));
            resp.setHeader("Location", req.getRequestURI() + "/" + criado.getId());
            JsonUtil.write(resp, 201, criado);
        } else if (p.length == 2 && LEITURAS.equals(p[1])) {
            int alertaId = toInt(p[0], "id");
            List<Integer> ids = idsDoCorpo(body(req, VinculoLeituraInput.class));
            JsonUtil.write(resp, 201, service.vincularLeituras(alertaId, ids));
        } else if (p.length == 2 && REGIOES.equals(p[1])) {
            int alertaId = toInt(p[0], "id");
            RegiaoAfetada criada = service.adicionarRegiao(alertaId, body(req, RegiaoAfetada.class));
            resp.setHeader("Location", req.getRequestURI() + "/" + criada.getId());
            JsonUtil.write(resp, 201, criada);
        } else {
            throw ApiException.badRequest("Rota nao suportada: " + req.getPathInfo());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] p = parts(req);
        if (p.length == 1) {
            JsonUtil.write(resp, 200, service.atualizar(toInt(p[0], "id"), body(req, Alerta.class)));
        } else if (p.length == 3 && REGIOES.equals(p[1])) {
            JsonUtil.write(resp, 200, service.atualizarRegiao(
                    toInt(p[0], "id"), toInt(p[2], "regiaoId"), body(req, RegiaoAfetada.class)));
        } else {
            throw ApiException.badRequest("Informe o id na URL (ex.: /alertas/1)");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String[] p = parts(req);
        if (p.length == 1) {
            service.remover(toInt(p[0], "id"));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (p.length == 3 && LEITURAS.equals(p[1])) {
            service.desvincularLeitura(toInt(p[0], "id"), toInt(p[2], "leituraId"));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (p.length == 3 && REGIOES.equals(p[1])) {
            service.removerRegiao(toInt(p[0], "id"), toInt(p[2], "regiaoId"));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            throw ApiException.badRequest("Rota nao suportada: " + req.getPathInfo());
        }
    }

    /** Quebra o {@code pathInfo} em segmentos (ex.: {@code /1/leituras} -> [1, leituras]). */
    private String[] parts(HttpServletRequest req) {
        String info = req.getPathInfo();
        if (info == null) {
            return new String[0];
        }
        String trimmed = info.replaceAll("^/+", "").replaceAll("/+$", "");
        return trimmed.isEmpty() ? new String[0] : trimmed.split("/+");
    }

    private int toInt(String valor, String campo) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw ApiException.badRequest(campo + " invalido: '" + valor + "'");
        }
    }

    private List<Integer> idsDoCorpo(VinculoLeituraInput input) {
        if (input == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        List<Integer> ids = new ArrayList<>();
        if (input.getLeituraIds() != null) {
            ids.addAll(input.getLeituraIds());
        }
        if (input.getLeituraId() != null) {
            ids.add(input.getLeituraId());
        }
        return ids;
    }
}
