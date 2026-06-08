package com.geoalerta.api.controller;

import com.geoalerta.api.util.ApiException;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet base da camada Controller. Centraliza:
 * <ul>
 *   <li>cabecalhos CORS (consumo pelo app mobile / navegadores);</li>
 *   <li>content-type JSON e UTF-8;</li>
 *   <li>tratamento uniforme de erros via {@link ApiException};</li>
 *   <li>extracao do identificador presente no caminho ({@code /recurso/{id}}).</li>
 * </ul>
 */
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {
            super.service(req, resp);
        } catch (ApiException e) {
            JsonUtil.writeError(resp, e.getStatus(), e.getMessage());
        } catch (Exception e) {
            JsonUtil.writeError(resp, 500, "Erro interno: " + e.getMessage());
        }
    }

    /**
     * Retorna o segmento de caminho apos o nome do recurso, ou {@code null}
     * quando a requisicao aponta para a colecao (ex.: {@code /alertas}).
     */
    protected String pathSegment(HttpServletRequest req) {
        String info = req.getPathInfo();
        if (info == null) {
            return null;
        }
        String trimmed = info.replaceAll("^/+", "").replaceAll("/+$", "");
        return trimmed.isEmpty() ? null : trimmed;
    }

    /** Mesmo que {@link #pathSegment} porem convertido para inteiro. */
    protected Integer pathId(HttpServletRequest req) {
        String seg = pathSegment(req);
        if (seg == null) {
            return null;
        }
        try {
            return Integer.valueOf(seg);
        } catch (NumberFormatException e) {
            throw ApiException.badRequest("ID invalido: '" + seg + "'");
        }
    }

    /** Exige a presenca de um id inteiro no caminho. */
    protected int requireId(HttpServletRequest req) {
        Integer id = pathId(req);
        if (id == null) {
            throw ApiException.badRequest("Informe o id na URL (ex.: /alertas/1)");
        }
        return id;
    }

    /** Exige a presenca de um identificador textual no caminho. */
    protected String requireSegment(HttpServletRequest req, String exemplo) {
        String seg = pathSegment(req);
        if (seg == null) {
            throw ApiException.badRequest("Informe o identificador na URL (ex.: " + exemplo + ")");
        }
        return seg;
    }

    /** Desserializa o corpo da requisicao para o tipo informado. */
    protected <T> T body(HttpServletRequest req, Class<T> type) throws IOException {
        return JsonUtil.read(req.getReader(), type);
    }
}
