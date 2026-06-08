package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.EmpresaAgricola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO da entidade {@link EmpresaAgricola}. A chave primaria e o CNPJ.
 */
public class EmpresaRepository {

    public List<EmpresaAgricola> findAll() throws SQLException {
        String sql = "SELECT cnpj, nomefantasia, email FROM empresaagricola ORDER BY nomefantasia";
        List<EmpresaAgricola> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        }
        return result;
    }

    public Optional<EmpresaAgricola> findByCnpj(String cnpj) throws SQLException {
        String sql = "SELECT cnpj, nomefantasia, email FROM empresaagricola WHERE cnpj = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    public boolean exists(String cnpj) throws SQLException {
        String sql = "SELECT 1 FROM empresaagricola WHERE cnpj = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insert(EmpresaAgricola e) throws SQLException {
        String sql = "INSERT INTO empresaagricola (cnpj, nomefantasia, email, senha_hash) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getCnpj());
            ps.setString(2, e.getNomeFantasia());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getSenhaHash());
            ps.executeUpdate();
        }
    }

    /** Atualiza nome e email; a senha so e alterada quando {@code senhaHash != null}. */
    public boolean update(String cnpj, EmpresaAgricola e) throws SQLException {
        boolean trocaSenha = e.getSenhaHash() != null;
        String sql = trocaSenha
                ? "UPDATE empresaagricola SET nomefantasia = ?, email = ?, senha_hash = ? WHERE cnpj = ?"
                : "UPDATE empresaagricola SET nomefantasia = ?, email = ? WHERE cnpj = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNomeFantasia());
            ps.setString(2, e.getEmail());
            if (trocaSenha) {
                ps.setString(3, e.getSenhaHash());
                ps.setString(4, cnpj);
            } else {
                ps.setString(3, cnpj);
            }
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(String cnpj) throws SQLException {
        String sql = "DELETE FROM empresaagricola WHERE cnpj = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            return ps.executeUpdate() > 0;
        }
    }

    private EmpresaAgricola map(ResultSet rs) throws SQLException {
        EmpresaAgricola e = new EmpresaAgricola();
        e.setCnpj(rs.getString("cnpj"));
        e.setNomeFantasia(rs.getString("nomefantasia"));
        e.setEmail(rs.getString("email"));
        return e;
    }
}
