package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.RegiaoAfetada;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO da entidade {@link RegiaoAfetada} (tabela {@code regioesafetadas}).
 */
public class RegiaoAfetadaRepository {

    private static final String COLUMNS = "id, alerta_id, erd_pluscode";

    public List<RegiaoAfetada> findByAlerta(int alertaId) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM regioesafetadas WHERE alerta_id = ? ORDER BY id";
        List<RegiaoAfetada> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alertaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        }
        return result;
    }

    public Optional<RegiaoAfetada> findById(int id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM regioesafetadas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public RegiaoAfetada insert(RegiaoAfetada r) throws SQLException {
        String sql = "INSERT INTO regioesafetadas (alerta_id, erd_pluscode) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getAlertaId());
            ps.setString(2, r.getErdPlusCode());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    r.setId(keys.getInt(1));
                }
            }
        }
        return r;
    }

    /** Atualiza o Plus Code e devolve {@code true} se a regiao existia. */
    public boolean update(int id, RegiaoAfetada r) throws SQLException {
        String sql = "UPDATE regioesafetadas SET erd_pluscode = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getErdPlusCode());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM regioesafetadas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private RegiaoAfetada map(ResultSet rs) throws SQLException {
        RegiaoAfetada r = new RegiaoAfetada();
        r.setId(rs.getInt("id"));
        r.setAlertaId(rs.getInt("alerta_id"));
        r.setErdPlusCode(rs.getString("erd_pluscode"));
        return r;
    }
}
