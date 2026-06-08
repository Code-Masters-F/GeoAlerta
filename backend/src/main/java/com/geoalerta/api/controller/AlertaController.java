package com.geoalerta.api.controller;

import com.geoalerta.api.model.Alerta;
import com.geoalerta.api.service.AlertaService;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller REST de Alertas.
 *
 * <pre>
 *   GET    /alertas        lista todos os alertas
 *   GET    /alertas/{id}   busca um alerta
 *   POST   /alertas        cria um alerta
 *   PUT    /alertas/{id}   atualiza um alerta
 *   DELETE /alertas/{id}   remove um alerta
 * </pre>
 */
@WebServlet(name = "AlertaController", urlPatterns = {"/alertas", "/alertas/*"})
public class AlertaController extends BaseServlet {

    private final transient AlertaService service = new AlertaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Integer id = pathId(req);
        if (id == null) {
            JsonUtil.write(resp, 200, service.listar());
        } else {
            JsonUtil.write(resp, 200, service.buscar(id));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Alerta criado = service.criar(body(req, Alerta.class));
        resp.setHeader("Location", req.getRequestURI() + "/" + criado.getId());
        JsonUtil.write(resp, 201, criado);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = requireId(req);
        JsonUtil.write(resp, 200, service.atualizar(id, body(req, Alerta.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int id = requireId(req);
        service.remover(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
