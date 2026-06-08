package com.geoalerta.api.controller;

import com.geoalerta.api.model.SensorIOT;
import com.geoalerta.api.service.SensorService;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller REST de Sensores IoT.
 *
 * <pre>
 *   GET    /sensores        lista todos os sensores
 *   GET    /sensores/{id}   busca um sensor
 *   POST   /sensores        cadastra um sensor
 *   PUT    /sensores/{id}   atualiza um sensor
 *   DELETE /sensores/{id}   remove um sensor
 * </pre>
 */
@WebServlet(name = "SensorController", urlPatterns = {"/sensores", "/sensores/*"})
public class SensorController extends BaseServlet {

    private final transient SensorService service = new SensorService();

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
        SensorIOT criado = service.criar(body(req, SensorIOT.class));
        resp.setHeader("Location", req.getRequestURI() + "/" + criado.getId());
        JsonUtil.write(resp, 201, criado);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = requireId(req);
        JsonUtil.write(resp, 200, service.atualizar(id, body(req, SensorIOT.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        int id = requireId(req);
        service.remover(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
