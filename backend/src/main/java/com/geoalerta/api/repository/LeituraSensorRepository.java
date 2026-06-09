package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.LeituraSensor;

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
 * DAO da entidade {@link LeituraSensor} (tabela {@code leiturasensor}).
 */
public class LeituraSensorRepository {

    private static final String COLUMNS =
            "id, sensor_id, valor, unidade_medida, data_hora";

    public List<LeituraSensor> findAll() throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM leiturasensor ORDER BY data_hora DESC";
        List<LeituraSensor> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        }
        return result;
    }

    public List<LeituraSensor> findBySensor(int sensorId) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM leiturasensor WHERE sensor_id = ? "
                + "ORDER BY data_hora DESC";
        List<LeituraSensor> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sensorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        }
        return result;
    }

    public Optional<LeituraSensor> findById(int id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM leiturasensor WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public LeituraSensor insert(LeituraSensor l) throws SQLException {
        String sql = "INSERT INTO leiturasensor (sensor_id, valor, unidade_medida, data_hora) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindWritable(ps, l);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    l.setId(keys.getInt(1));
                }
            }
        }
        return l;
    }

    /** Atualiza e devolve {@code true} se a leitura existia. */
    public boolean update(int id, LeituraSensor l) throws SQLException {
        String sql = "UPDATE leiturasensor SET sensor_id = ?, valor = ?, unidade_medida = ?, "
                + "data_hora = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindWritable(ps, l);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Remove e devolve {@code true} se a leitura existia. */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM leiturasensor WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private void bindWritable(PreparedStatement ps, LeituraSensor l) throws SQLException {
        ps.setInt(1, l.getSensorId());
        ps.setBigDecimal(2, l.getValor());
        ps.setString(3, l.getUnidadeMedida());
        ps.setTimestamp(4, Timestamp.valueOf(l.getDataHora()));
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
