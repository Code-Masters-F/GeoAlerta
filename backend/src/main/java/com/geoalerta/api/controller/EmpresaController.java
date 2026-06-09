package com.geoalerta.api.controller;

import com.geoalerta.api.model.EmpresaInput;
import com.geoalerta.api.model.Endereco;
import com.geoalerta.api.model.VinculoAlertaInput;
import com.geoalerta.api.service.EmpresaService;
import com.geoalerta.api.util.ApiException;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller REST de Empresas Agricolas (identificadas pelo CNPJ).
 *
 * <pre>
 *   GET    /empresas                            lista todas as empresas
 *   GET    /empresas/{cnpj}                     busca uma empresa
 *   POST   /empresas                            cadastra uma empresa
 *   PUT    /empresas/{cnpj}                     atualiza uma empresa
 *   DELETE /empresas/{cnpj}                     remove uma empresa
 *
 *   GET    /empresas/{cnpj}/notificacoes        lista os alertas notificados a empresa
 *   POST   /empresas/{cnpj}/notificacoes        registra notificacao de alerta(s)
 *   DELETE /empresas/{cnpj}/notificacoes/{aid}  remove a notificacao de um alerta
 *
 *   GET    /empresas/{cnpj}/enderecos           lista os enderecos da empresa
 *   GET    /empresas/{cnpj}/enderecos/{id}      busca um endereco
 *   POST   /empresas/{cnpj}/enderecos           adiciona um endereco
 *   PUT    /empresas/{cnpj}/enderecos/{id}      atualiza um endereco
 *   DELETE /empresas/{cnpj}/enderecos/{id}      remove um endereco
 * </pre>
 */
@WebServlet(name = "EmpresaController", urlPatterns = {"/empresas", "/empresas/*"})
public class EmpresaController extends BaseServlet {

    private static final String NOTIFICACOES = "notificacoes";
    private static final String ENDERECOS = "enderecos";

    private final transient EmpresaService service = new EmpresaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String[] p = parts(req);
        if (p.length == 0) {
            JsonUtil.write(resp, 200, service.listar());
        } else if (p.length == 1) {
            JsonUtil.write(resp, 200, service.buscar(p[0]));
        } else if (p.length == 2 && NOTIFICACOES.equals(p[1])) {
            JsonUtil.write(resp, 200, service.listarNotificacoes(p[0]));
        } else if (p.length == 2 && ENDERECOS.equals(p[1])) {
            JsonUtil.write(resp, 200, service.listarEnderecos(p[0]));
        } else if (p.length == 3 && ENDERECOS.equals(p[1])) {
            JsonUtil.write(resp, 200, service.buscarEndereco(p[0], toInt(p[2], "id")));
        } else {
            throw ApiException.badRequest("Rota nao suportada: " + req.getPathInfo());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] p = parts(req);
        if (p.length == 0) {
            var criada = service.criar(body(req, EmpresaInput.class));
            resp.setHeader("Location", req.getRequestURI() + "/" + criada.getCnpj());
            JsonUtil.write(resp, 201, criada);
        } else if (p.length == 2 && NOTIFICACOES.equals(p[1])) {
            List<Integer> ids = idsDoCorpo(body(req, VinculoAlertaInput.class));
            JsonUtil.write(resp, 201, service.registrarNotificacoes(p[0], ids));
        } else if (p.length == 2 && ENDERECOS.equals(p[1])) {
            Endereco criado = service.adicionarEndereco(p[0], body(req, Endereco.class));
            resp.setHeader("Location", req.getRequestURI() + "/" + criado.getId());
            JsonUtil.write(resp, 201, criado);
        } else {
            throw ApiException.badRequest("Rota nao suportada: " + req.getPathInfo());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String[] p = parts(req);
        if (p.length == 1) {
            JsonUtil.write(resp, 200, service.atualizar(p[0], body(req, EmpresaInput.class)));
        } else if (p.length == 3 && ENDERECOS.equals(p[1])) {
            JsonUtil.write(resp, 200, service.atualizarEndereco(
                    p[0], toInt(p[2], "id"), body(req, Endereco.class)));
        } else {
            throw ApiException.badRequest("Informe o CNPJ na URL (ex.: /empresas/11222333000181)");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String[] p = parts(req);
        if (p.length == 1) {
            service.remover(p[0]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (p.length == 3 && NOTIFICACOES.equals(p[1])) {
            service.removerNotificacao(p[0], toInt(p[2], "alertaId"));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (p.length == 3 && ENDERECOS.equals(p[1])) {
            service.removerEndereco(p[0], toInt(p[2], "id"));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            throw ApiException.badRequest("Rota nao suportada: " + req.getPathInfo());
        }
    }

    /** Quebra o {@code pathInfo} em segmentos (ex.: {@code /123/notificacoes}). */
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

    private List<Integer> idsDoCorpo(VinculoAlertaInput input) {
        if (input == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        List<Integer> ids = new ArrayList<>();
        if (input.getAlertaIds() != null) {
            ids.addAll(input.getAlertaIds());
        }
        if (input.getAlertaId() != null) {
            ids.add(input.getAlertaId());
        }
        return ids;
    }
}
