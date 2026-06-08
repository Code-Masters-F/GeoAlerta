package com.geoalerta.api.service;

import com.geoalerta.api.model.EmpresaAgricola;
import com.geoalerta.api.model.EmpresaInput;
import com.geoalerta.api.repository.EmpresaRepository;
import com.geoalerta.api.util.ApiException;
import com.geoalerta.api.util.Passwords;

import java.sql.SQLException;
import java.util.List;

/**
 * Regras de negocio da entidade {@link EmpresaAgricola}: validacao de CNPJ,
 * e-mail e senha, alem do hashing da senha antes da persistencia.
 */
public class EmpresaService {

    private final EmpresaRepository repository = new EmpresaRepository();

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
