package com.geoalerta.api.service;

import com.geoalerta.api.model.Alerta;
import com.geoalerta.api.model.EmpresaAgricola;
import com.geoalerta.api.model.EmpresaInput;
import com.geoalerta.api.model.Endereco;
import com.geoalerta.api.repository.AlertaRepository;
import com.geoalerta.api.repository.EmpresaRepository;
import com.geoalerta.api.repository.EnderecoRepository;
import com.geoalerta.api.repository.NotificacaoRepository;
import com.geoalerta.api.util.ApiException;
import com.geoalerta.api.util.Passwords;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Regras de negocio da entidade {@link EmpresaAgricola}: validacao de CNPJ,
 * e-mail e senha, alem do hashing da senha antes da persistencia.
 */
public class EmpresaService {

    private final EmpresaRepository repository = new EmpresaRepository();
    private final NotificacaoRepository notificacaoRepository = new NotificacaoRepository();
    private final AlertaRepository alertaRepository = new AlertaRepository();
    private final EnderecoRepository enderecoRepository = new EnderecoRepository();

    public List<EmpresaAgricola> listar() {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public EmpresaAgricola buscar(String cnpj) {
        try {
            return repository.findByCnpj(cnpj)
                    .orElseThrow(() -> ApiException.notFound("Empresa " + cnpj + " nao encontrada"));
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public EmpresaAgricola criar(EmpresaInput input) {
        if (input == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        String cnpj = normalizarCnpj(input.getCnpj());
        exigirTexto(input.getNomeFantasia(), "nomeFantasia", 60);
        validarEmail(input.getEmail());
        exigirSenha(input.getSenha());

        try {
            if (repository.exists(cnpj)) {
                throw ApiException.conflict("Ja existe empresa com o CNPJ " + cnpj);
            }
            EmpresaAgricola empresa = new EmpresaAgricola();
            empresa.setCnpj(cnpj);
            empresa.setNomeFantasia(input.getNomeFantasia());
            empresa.setEmail(input.getEmail());
            empresa.setSenhaHash(Passwords.hash(input.getSenha()));
            repository.insert(empresa);
            return empresa;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public EmpresaAgricola atualizar(String cnpj, EmpresaInput input) {
        if (input == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        exigirTexto(input.getNomeFantasia(), "nomeFantasia", 60);
        validarEmail(input.getEmail());

        EmpresaAgricola empresa = new EmpresaAgricola();
        empresa.setCnpj(cnpj);
        empresa.setNomeFantasia(input.getNomeFantasia());
        empresa.setEmail(input.getEmail());
        if (input.getSenha() != null && !input.getSenha().isBlank()) {
            empresa.setSenhaHash(Passwords.hash(input.getSenha()));
        }

        try {
            if (!repository.update(cnpj, empresa)) {
                throw ApiException.notFound("Empresa " + cnpj + " nao encontrada");
            }
            return empresa;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    public void remover(String cnpj) {
        try {
            if (!repository.delete(cnpj)) {
                throw ApiException.notFound("Empresa " + cnpj + " nao encontrada");
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    // ----- Notificacoes de alertas recebidas (tabela notificacoesrecebidas) -----

    /** Lista os alertas notificados a empresa. */
    public List<Alerta> listarNotificacoes(String cnpj) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        try {
            return notificacaoRepository.findAlertasByEmpresa(c);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /**
     * Registra a notificacao de um ou mais alertas para a empresa e devolve os
     * alertas registrados. Vinculos ja existentes sao ignorados (idempotente).
     */
    public List<Alerta> registrarNotificacoes(String cnpj, List<Integer> alertaIds) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        if (alertaIds == null || alertaIds.isEmpty()) {
            throw ApiException.badRequest("Informe 'alertaId' ou 'alertaIds'");
        }
        List<Alerta> registrados = new ArrayList<>();
        try {
            for (Integer alertaId : alertaIds) {
                if (alertaId == null) {
                    throw ApiException.badRequest("Lista de alertas contem id nulo");
                }
                Alerta alerta = alertaRepository.findById(alertaId)
                        .orElseThrow(() -> ApiException.badRequest(
                                "Alerta " + alertaId + " nao encontrado"));
                if (!notificacaoRepository.exists(c, alertaId)) {
                    notificacaoRepository.link(c, alertaId);
                }
                registrados.add(alerta);
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
        return registrados;
    }

    /** Remove a notificacao de um alerta para a empresa. */
    public void removerNotificacao(String cnpj, int alertaId) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        try {
            if (!notificacaoRepository.unlink(c, alertaId)) {
                throw ApiException.notFound("Alerta " + alertaId
                        + " nao notificado a empresa " + c);
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    // ----- Enderecos da empresa (tabela enderecos) -----

    /** Lista os enderecos da empresa. */
    public List<Endereco> listarEnderecos(String cnpj) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        try {
            return enderecoRepository.findByCnpj(c);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Busca um endereco da empresa. */
    public Endereco buscarEndereco(String cnpj, int id) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        try {
            return enderecoDaEmpresa(c, id);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Adiciona um endereco a empresa. */
    public Endereco adicionarEndereco(String cnpj, Endereco endereco) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        validarEndereco(endereco);
        endereco.setCnpj(c);
        try {
            return enderecoRepository.insert(endereco);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Atualiza o Plus Code de um endereco da empresa. */
    public Endereco atualizarEndereco(String cnpj, int id, Endereco endereco) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        validarEndereco(endereco);
        try {
            enderecoDaEmpresa(c, id);
            enderecoRepository.update(id, endereco);
            endereco.setId(id);
            endereco.setCnpj(c);
            return endereco;
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Remove um endereco da empresa. */
    public void removerEndereco(String cnpj, int id) {
        String c = normalizarCnpj(cnpj);
        exigirEmpresa(c);
        try {
            enderecoDaEmpresa(c, id);
            enderecoRepository.delete(id);
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    /** Garante que o endereco existe e pertence a empresa informada. */
    private Endereco enderecoDaEmpresa(String cnpj, int id) throws SQLException {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Endereco " + id + " nao encontrado"));
        if (!cnpj.equals(endereco.getCnpj().trim())) {
            throw ApiException.notFound("Endereco " + id + " nao pertence a empresa " + cnpj);
        }
        return endereco;
    }

    private void validarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw ApiException.badRequest("Corpo da requisicao ausente");
        }
        exigirTexto(endereco.getErdPlusCode(), "erdPlusCode", 11);
    }

    private void exigirEmpresa(String cnpj) {
        try {
            if (!repository.exists(cnpj)) {
                throw ApiException.notFound("Empresa " + cnpj + " nao encontrada");
            }
        } catch (SQLException e) {
            throw erroBanco(e);
        }
    }

    private String normalizarCnpj(String cnpj) {
        if (cnpj == null) {
            throw ApiException.badRequest("O campo 'cnpj' e obrigatorio");
        }
        String somenteDigitos = cnpj.replaceAll("\\D", "");
        if (somenteDigitos.length() != 14) {
            throw ApiException.badRequest("CNPJ deve conter 14 digitos");
        }
        return somenteDigitos;
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw ApiException.badRequest("O campo 'email' e obrigatorio");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw ApiException.badRequest("E-mail invalido");
        }
        if (email.length() > 255) {
            throw ApiException.badRequest("E-mail excede 255 caracteres");
        }
    }

    private void exigirSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw ApiException.badRequest("A senha deve ter ao menos 6 caracteres");
        }
    }

    private void exigirTexto(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            throw ApiException.badRequest("O campo '" + campo + "' e obrigatorio");
        }
        if (valor.length() > max) {
            throw ApiException.badRequest("O campo '" + campo + "' excede " + max + " caracteres");
        }
    }

    private ApiException erroBanco(SQLException e) {
        return new ApiException(500, "Erro de banco de dados: " + e.getMessage());
    }
}
