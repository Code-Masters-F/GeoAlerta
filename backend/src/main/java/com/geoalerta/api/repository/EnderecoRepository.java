package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO da entidade {@link Endereco} (tabela {@code enderecos}).
 */
public class EnderecoRepository {

    private static final String COLUMNS = "id, cnpj, erd_pluscode";

    public List<Endereco> findByCnpj(String cnpj) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM enderecos WHERE cnpj = ? ORDER BY id";
        List<Endereco> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        }
        return result;
    }

    public Optional<Endereco> findById(int id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM enderecos WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public Endereco insert(Endereco e) throws SQLException {
        String sql = "INSERT INTO enderecos (cnpj, erd_pluscode) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getCnpj());
            ps.setString(2, e.getErdPlusCode());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    e.setId(keys.getInt(1));
                }
            }
        }
        return e;
    }

    /** Atualiza o Plus Code e devolve {@code true} se o endereco existia. */
    public boolean update(int id, Endereco e) throws SQLException {
        String sql = "UPDATE enderecos SET erd_pluscode = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getErdPlusCode());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM enderecos WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Endereco map(ResultSet rs) throws SQLException {
        Endereco e = new Endereco();
        e.setId(rs.getInt("id"));
        e.setCnpj(rs.getString("cnpj"));
        e.setErdPlusCode(rs.getString("erd_pluscode"));
        return e;
    }
}
