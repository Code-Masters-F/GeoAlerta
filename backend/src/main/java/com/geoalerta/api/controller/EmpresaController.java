package com.geoalerta.api.controller;

import com.geoalerta.api.model.EmpresaInput;
import com.geoalerta.api.service.EmpresaService;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controller REST de Empresas Agricolas (identificadas pelo CNPJ).
 *
 * <pre>
 *   GET    /empresas          lista todas as empresas
 *   GET    /empresas/{cnpj}   busca uma empresa
 *   POST   /empresas          cadastra uma empresa
 *   PUT    /empresas/{cnpj}   atualiza uma empresa
 *   DELETE /empresas/{cnpj}   remove uma empresa
 * </pre>
 */
@WebServlet(name = "EmpresaController", urlPatterns = {"/empresas", "/empresas/*"})
public class EmpresaController extends BaseServlet {

    private final transient EmpresaService service = new EmpresaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String cnpj = pathSegment(req);
        if (cnpj == null) {
            JsonUtil.write(resp, 200, service.listar());
        } else {
            JsonUtil.write(resp, 200, service.buscar(cnpj));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var criada = service.criar(body(req, EmpresaInput.class));
        resp.setHeader("Location", req.getRequestURI() + "/" + criada.getCnpj());
        JsonUtil.write(resp, 201, criada);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cnpj = requireSegment(req, "/empresas/11222333000181");
        JsonUtil.write(resp, 200, service.atualizar(cnpj, body(req, EmpresaInput.class)));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String cnpj = requireSegment(req, "/empresas/11222333000181");
        service.remover(cnpj);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
