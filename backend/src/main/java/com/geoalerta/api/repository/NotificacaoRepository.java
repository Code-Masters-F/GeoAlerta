package com.geoalerta.api.repository;

import com.geoalerta.api.config.Database;
import com.geoalerta.api.model.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO da tabela de juncao {@code notificacoesrecebidas}, que liga
 * {@code empresaagricola} (CNPJ) aos {@code alertas} que ela recebeu.
 */
public class NotificacaoRepository {

    /** Alertas notificados a uma empresa, do mais recente para o mais antigo. */
    public List<Alerta> findAlertasByEmpresa(String cnpj) throws SQLException {
        String sql = "SELECT a.id, a.nome, a.tipo, a.grau_gravidade, a.data_de_emissao, a.descricao "
                + "FROM alertas a "
                + "JOIN notificacoesrecebidas nr ON nr.alerta_id = a.id "
                + "WHERE nr.cnpj = ? "
                + "ORDER BY a.data_de_emissao DESC";
        List<Alerta> result = new ArrayList<>();
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

    public boolean exists(String cnpj, int alertaId) throws SQLException {
        String sql = "SELECT 1 FROM notificacoesrecebidas WHERE cnpj = ? AND alerta_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            ps.setInt(2, alertaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void link(String cnpj, int alertaId) throws SQLException {
        String sql = "INSERT INTO notificacoesrecebidas (cnpj, alerta_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            ps.setInt(2, alertaId);
            ps.executeUpdate();
        }
    }

    /** Remove a notificacao e devolve {@code true} se ela existia. */
    public boolean unlink(String cnpj, int alertaId) throws SQLException {
        String sql = "DELETE FROM notificacoesrecebidas WHERE cnpj = ? AND alerta_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cnpj);
            ps.setInt(2, alertaId);
            return ps.executeUpdate() > 0;
        }
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
