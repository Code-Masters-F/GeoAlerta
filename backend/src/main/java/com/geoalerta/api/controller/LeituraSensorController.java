package com.geoalerta.api.controller;

import com.geoalerta.api.model.LeituraSensor;
import com.geoalerta.api.service.LeituraSensorService;
import com.geoalerta.api.util.ApiException;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller REST de Leituras de Sensores.
 *
 * <pre>
 *   GET    /leituras              lista todas as leituras
 *   GET    /leituras?sensorId={n} lista as leituras de um sensor
 *   GET    /leituras/{id}         busca uma leitura
 *   POST   /leituras              cadastra uma leitura
 *   PUT    /leituras/{id}         atualiza uma leitura
 *   DELETE /leituras/{id}         remove uma leitura
 * </pre>
 */
@WebServlet(name = "LeituraSensorController", urlPatterns = {"/leituras", "/leituras/*"})
public class LeituraSensorController extends BaseServlet {

    private final transient LeituraSensorService service = new LeituraSensorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Integer id = pathId(req);
        if (id != null) {
            JsonUtil.write(resp, 200, service.buscar(id));
            return;
        }
        Integer sensorId = queryInt(req, "sensorId");
        if (sensorId != null) {
            JsonUtil.write(resp, 200, service.listarPorSensor(sensorId));
        } else {
            JsonUtil.write(resp, 200, service.listar());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LeituraSensor criada = service.criar(body(req, LeituraSensor.class));
        resp.setHeader("Location", req.getRequestURI() + "/" + criada.getId());
        JsonUtil.write(resp, 201, criada);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = requireId(req);
        JsonUtil.write(resp, 200, service.atualizar(id, body(req, LeituraSensor.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int id = requireId(req);
        service.remover(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Integer queryInt(HttpServletRequest req, String nome) {
        String raw = req.getParameter(nome);
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(raw.trim());
        } catch (NumberFormatException e) {
            throw ApiException.badRequest(nome + " invalido: '" + raw + "'");
        }
    }
}
