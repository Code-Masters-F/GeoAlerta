package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.LeituraSensor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO da tabela de juncao {@code sensoresalerta}, que liga {@code alertas} as
 * {@code leiturasensor} que originaram cada alerta.
 */
public class SensorAlertaRepository {

    /** Leituras vinculadas a um alerta, da mais recente para a mais antiga. */
    public List<LeituraSensor> findLeiturasByAlerta(int alertaId) throws SQLException {
        String sql = "SELECT l.id, l.sensor_id, l.valor, l.unidade_medida, l.data_hora "
                + "FROM leiturasensor l "
                + "JOIN sensoresalerta sa ON sa.leitura_id = l.id "
                + "WHERE sa.alerta_id = ? "
                + "ORDER BY l.data_hora DESC";
        List<LeituraSensor> result = new ArrayList<>();
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

    public boolean exists(int alertaId, int leituraId) throws SQLException {
        String sql = "SELECT 1 FROM sensoresalerta WHERE alerta_id = ? AND leitura_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alertaId);
            ps.setInt(2, leituraId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void link(int alertaId, int leituraId) throws SQLException {
        String sql = "INSERT INTO sensoresalerta (alerta_id, leitura_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alertaId);
            ps.setInt(2, leituraId);
            ps.executeUpdate();
        }
    }

    /** Remove o vinculo e devolve {@code true} se ele existia. */
    public boolean unlink(int alertaId, int leituraId) throws SQLException {
        String sql = "DELETE FROM sensoresalerta WHERE alerta_id = ? AND leitura_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, alertaId);
            ps.setInt(2, leituraId);
            return ps.executeUpdate() > 0;
        }
    }

    private LeituraSensor map(ResultSet rs) throws SQLException {
        LeituraSensor l = new LeituraSensor();
        l.setId(rs.getInt("id"));
        l.setSensorId(rs.getInt("sensor_id"));
        l.setValor(rs.getBigDecimal("valor"));
        l.setUnidadeMedida(rs.getString("unidade_medida"));
        Timestamp ts = rs.getTimestamp("data_hora");
        l.setDataHora(ts != null ? ts.toLocalDateTime() : null);
        return l;
    }
}
