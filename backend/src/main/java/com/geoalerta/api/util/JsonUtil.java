package com.geoalerta.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Utilitario central de (de)serializacao JSON e escrita de respostas HTTP.
 * Mantem uma unica instancia configurada de {@link Gson} com suporte a
 * {@link LocalDateTime} no formato ISO-8601.
 */
public final class JsonUtil {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter out, LocalDateTime value) throws IOException {
                    if (value == null) {
                        out.nullValue();
                    } else {
                        out.value(value.format(ISO));
                    }
                }

                @Override
                public LocalDateTime read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    return parseDateTime(in.nextString());
                }
            })
            .serializeNulls()
            .create();

    private JsonUtil() {
    }

    public static Gson gson() {
        return GSON;
    }

    /** Le e desserializa o corpo da requisicao para o tipo informado. */
    public static <T> T read(Reader body, Class<T> type) {
        try {
            T obj = GSON.fromJson(body, type);
            if (obj == null) {
                throw ApiException.badRequest("Corpo da requisicao vazio ou invalido");
            }
            return obj;
        } catch (JsonSyntaxException | JsonIOException e) {
            throw ApiException.badRequest("JSON invalido: " + e.getMessage());
        }
    }

    /** Escreve um objeto qualquer como JSON com o status informado. */
    public static void write(HttpServletResponse resp, int status, Object body) {
        resp.setStatus(status);
        try (Writer w = resp.getWriter()) {
            GSON.toJson(body, w);
            w.flush();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao escrever resposta JSON", e);
        }
    }

    /** Resposta de erro padronizada: {"status":404,"erro":"..."}. */
    public static void writeError(HttpServletResponse resp, int status, String message) {
        write(resp, status, Map.of("status", status, "erro", message));
    }

    /**
     * Aceita datas em ISO ({@code 2026-05-10T06:30:00}), com espaco
     * ({@code 2026-05-10 06:30:00}) ou apenas data ({@code 2026-05-10}).
     */
    static LocalDateTime parseDateTime(String raw) {
        String s = raw.trim();
        if (s.endsWith("Z")) {
            s = s.substring(0, s.length() - 1);
        }
        s = s.replace(' ', 'T');
        if (s.length() == 10) {
            s = s + "T00:00:00";
        }
        try {
            return LocalDateTime.parse(s, ISO);
        } catch (DateTimeParseException e) {
            throw ApiException.badRequest("Data/hora invalida: '" + raw
                    + "'. Use o formato ISO (ex.: 2026-05-10T06:30:00)");
        }
    }
}
