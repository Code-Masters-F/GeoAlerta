package com.geoalerta.api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fabrica de conexoes JDBC com o PostgreSQL.
 *
 * <p>As credenciais sao lidas de variaveis de ambiente, com valores padrao
 * apontando para um PostgreSQL local. Assim a mesma aplicacao roda tanto
 * localmente quanto contra o banco do Supabase apenas trocando o ambiente:
 *
 * <pre>
 *   GEOALERTA_DB_URL       (ex.: jdbc:postgresql://localhost:5432/postgres)
 *   GEOALERTA_DB_USER      (ex.: postgres)
 *   GEOALERTA_DB_PASSWORD  (ex.: postgres)
 * </pre>
 */
public final class Database {

    private static final String URL =
            env("GEOALERTA_DB_URL", "jdbc:postgresql://localhost:5432/postgres");
    private static final String USER =
            env("GEOALERTA_DB_USER", "postgres");
    private static final String PASSWORD =
            env("GEOALERTA_DB_PASSWORD", "postgres");

    static {
        try {
            // Garante o registro do driver mesmo em containers que nao
            // fazem auto-discovery via ServiceLoader.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Driver JDBC do PostgreSQL nao encontrado no classpath");
        }
    }

    private Database() {
    }

    /**
     * Abre uma nova conexao. O chamador (camada Repository) e responsavel
     * por fecha-la, preferencialmente via try-with-resources.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
