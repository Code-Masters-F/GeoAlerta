package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.SensorIOT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO da entidade {@link SensorIOT} (tabela {@code sensoriot}).
 */
public class SensorRepository {

    private static final String COLUMNS =
            "id, tipo, erd_pluscode, status, data_instalacao";

    public List<SensorIOT> findAll() throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM sensoriot ORDER BY data_instalacao DESC";
        List<SensorIOT> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        }
        return result;
    }

    public Optional<SensorIOT> findById(int id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM sensoriot WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public SensorIOT insert(SensorIOT s) throws SQLException {
        String sql = "INSERT INTO sensoriot (tipo, erd_pluscode, status, data_instalacao) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindWritable(ps, s);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setId(keys.getInt(1));
                }
            }
        }
        return s;
    }

    public boolean update(int id, SensorIOT s) throws SQLException {
        String sql = "UPDATE sensoriot SET tipo = ?, erd_pluscode = ?, status = ?, "
                + "data_instalacao = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindWritable(ps, s);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM sensoriot WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private void bindWritable(PreparedStatement ps, SensorIOT s) throws SQLException {
        ps.setString(1, s.getTipo());
        ps.setString(2, s.getErdPlusCode());
        ps.setString(3, s.getStatus());
        ps.setTimestamp(4, Timestamp.valueOf(s.getDataInstalacao()));
    }

    private SensorIOT map(ResultSet rs) throws SQLException {
        SensorIOT s = new SensorIOT();
        s.setId(rs.getInt("id"));
        s.setTipo(rs.getString("tipo"));
        s.setErdPlusCode(rs.getString("erd_pluscode"));
        s.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("data_instalacao");
        s.setDataInstalacao(ts != null ? ts.toLocalDateTime() : null);
        return s;
    }
}
