package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.Alerta;

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
 * DAO da entidade {@link Alerta}. Encapsula todo o acesso SQL a tabela
 * {@code alertas}, isolando as camadas superiores do JDBC.
 */
public class AlertaRepository {

    private static final String COLUMNS =
            "id, nome, tipo, grau_gravidade, data_de_emissao, descricao";

    public List<Alerta> findAll() throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM alertas ORDER BY data_de_emissao DESC";
        List<Alerta> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        }
        return result;
    }

    public Optional<Alerta> findById(int id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM alertas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Alerta insert(Alerta a) throws SQLException {
        String sql = "INSERT INTO alertas (nome, tipo, grau_gravidade, data_de_emissao, descricao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindWritable(ps, a);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    a.setId(keys.getInt(1));
                }
            }
        }
        return a;
    }

    /** Atualiza e devolve {@code true} se o alerta existia. */
    public boolean update(int id, Alerta a) throws SQLException {
        String sql = "UPDATE alertas SET nome = ?, tipo = ?, grau_gravidade = ?, "
                + "data_de_emissao = ?, descricao = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindWritable(ps, a);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Remove e devolve {@code true} se o alerta existia. */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM alertas WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private void bindWritable(PreparedStatement ps, Alerta a) throws SQLException {
        ps.setString(1, a.getNome());
        ps.setString(2, a.getTipo());
        ps.setString(3, a.getGrauGravidade());
        ps.setTimestamp(4, Timestamp.valueOf(a.getDataDeEmissao()));
        ps.setString(5, a.getDescricao());
    }

    private Alerta map(ResultSet rs) throws SQLException {
        Alerta a = new Alerta();
        a.setId(rs.getInt("id"));
        a.setNome(rs.getString("nome"));
        a.setTipo(rs.getString("tipo"));
        a.setGrauGravidade(rs.getString("grau_gravidade"));
        Timestamp ts = rs.getTimestamp("data_de_emissao");
        a.setDataDeEmissao(ts != null ? ts.toLocalDateTime() : null);
        a.setDescricao(rs.getString("descricao"));
        return a;
    }
}
