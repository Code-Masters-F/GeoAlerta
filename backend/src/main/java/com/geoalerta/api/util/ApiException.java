package com.geoalerta.api.util;

/**
 * Excecao de aplicacao que carrega o codigo HTTP a ser devolvido ao cliente.
 * Usada pelas camadas Service/Controller para sinalizar erros de validacao
 * (400), recurso inexistente (404), conflito (409) etc.
 */
public class ApiException extends RuntimeException {

    private final int status;

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static ApiException badRequest(String message) {
        return new ApiException(400, message);
    }

    public static ApiException notFound(String message) {
        return new ApiException(404, message);
    }

    public static ApiException conflict(String message) {
        return new ApiException(409, message);
    }
}
