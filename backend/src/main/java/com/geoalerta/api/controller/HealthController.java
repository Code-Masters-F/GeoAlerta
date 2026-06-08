package com.geoalerta.api.controller;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Endpoint de saude/raiz. Util para validar o deploy e a conexao com o banco.
 *
 * <pre>
 *   GET /            descricao da API, recursos e links da documentacao
 *   GET /health      status da aplicacao e do banco de dados
 * </pre>
 *
 * <p>Mapeado apenas na raiz exata ({@code ""}) e em {@code /health} — sem o
 * padrao {@code "/"} — para que o Tomcat continue servindo os arquivos
 * estaticos da documentacao ({@code swagger.html}, {@code openapi.yaml}).
 */
@WebServlet(name = "HealthController", urlPatterns = {"", "/health"})
public class HealthController extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("aplicacao", "GeoAlerta API");
        body.put("versao", "1.0.0");
        body.put("bancoDeDados", testarBanco());
        String ctx = req.getContextPath();
        body.put("recursos", Map.of(
                "alertas", ctx + "/alertas",
                "empresas", ctx + "/empresas",
                "sensores", ctx + "/sensores"));
        body.put("documentacao", Map.of(
                "swaggerUI", ctx + "/swagger.html",
                "openapi", ctx + "/openapi.yaml"));
        JsonUtil.write(resp, 200, body);
    }

    private String testarBanco() {
        try (Connection conn = Database.getConnection()) {
            return conn.isValid(2) ? "conectado" : "indisponivel";
        } catch (Exception e) {
            return "indisponivel: " + e.getMessage();
        }
    }
}
