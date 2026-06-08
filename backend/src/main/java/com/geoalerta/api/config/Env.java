package com.geoalerta.api.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Leitor simples de configuracao com suporte a arquivo {@code .env}, sem
 * dependencias externas.
 *
 * <p>Ordem de precedencia ao resolver uma chave:
 * <ol>
 *   <li>variavel de ambiente real ({@code System.getenv});</li>
 *   <li>propriedade de sistema ({@code -Dchave=valor});</li>
 *   <li>arquivo {@code .env} (nao versionado);</li>
 *   <li>valor padrao informado pelo chamador.</li>
 * </ol>
 *
 * <p>O caminho do {@code .env} pode ser forcado via {@code GEOALERTA_ENV_FILE}
 * (variavel de ambiente ou propriedade de sistema). Caso contrario, busca-se
 * um arquivo {@code .env} a partir do diretorio de trabalho subindo ate 4
 * niveis de diretorio.
 */
public final class Env {

    private static final Map<String, String> DOTENV = carregarDotEnv();

    private Env() {
    }

    /** Resolve uma chave seguindo a ordem de precedencia; pode retornar {@code null}. */
    public static String get(String key) {
        String value = System.getenv(key);
        if (isBlank(value)) {
            value = System.getProperty(key);
        }
        if (isBlank(value)) {
            value = DOTENV.get(key);
        }
        return isBlank(value) ? null : value;
    }

    /** Igual a {@link #get(String)}, porem com valor padrao. */
    public static String get(String key, String fallback) {
        String value = get(key);
        return value == null ? fallback : value;
    }

    private static Map<String, String> carregarDotEnv() {
        Map<String, String> map = new HashMap<>();
        Path arquivo = localizarArquivo();
        if (arquivo == null) {
            return map;
        }
        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                String l = linha.strip();
                if (l.isEmpty() || l.startsWith("#")) {
                    continue;
                }
                if (l.startsWith("export ")) {
                    l = l.substring("export ".length()).strip();
                }
                int eq = l.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String chave = l.substring(0, eq).strip();
                String valor = desempacotar(l.substring(eq + 1).strip());
                map.put(chave, valor);
            }
        } catch (IOException e) {
            // .env opcional: se nao for legivel, segue com variaveis de ambiente/padroes.
            System.getLogger(Env.class.getName())
                    .log(System.Logger.Level.WARNING, "Nao foi possivel ler " + arquivo, e);
        }
        return map;
    }

    private static Path localizarArquivo() {
        String explicito = System.getenv("GEOALERTA_ENV_FILE");
        if (isBlank(explicito)) {
            explicito = System.getProperty("GEOALERTA_ENV_FILE");
        }
        if (!isBlank(explicito)) {
            Path p = Paths.get(explicito);
            return Files.isRegularFile(p) ? p : null;
        }
        Path dir = Paths.get("").toAbsolutePath();
        for (int i = 0; i < 5 && dir != null; i++) {
            Path candidato = dir.resolve(".env");
            if (Files.isRegularFile(candidato)) {
                return candidato;
            }
            dir = dir.getParent();
        }
        return null;
    }

    /** Remove aspas simples/duplas que envolvam todo o valor. */
    private static String desempacotar(String valor) {
        if (valor.length() >= 2
                && ((valor.startsWith("\"") && valor.endsWith("\""))
                    || (valor.startsWith("'") && valor.endsWith("'")))) {
            return valor.substring(1, valor.length() - 1);
        }
        return valor;
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    /** Util apenas para diagnostico: lista as chaves carregadas do {@code .env}. */
    static List<String> chavesDotEnv() {
        return List.copyOf(DOTENV.keySet());
    }
}
