package com.geoalerta.app.auth

/**
 * Repositório de autenticação com dados mockados (em memória), seguindo o
 * mesmo padrão do `MockRepository` de propriedades/alertas. As senhas são
 * armazenadas apenas como hash PBKDF2 + salt (ver [PasswordHasher]) — em
 * nenhum momento a senha em texto puro fica guardada.
 *
 * Empresa de demonstração:
 *  - CNPJ:  12.345.678/0001-95
 *  - Senha: GeoAlerta2026
 */
object AuthRepository {

    /** Dados públicos da empresa autenticada — nunca carrega o hash da senha. */
    data class Empresa(
        val nome: String,
        val cnpj: String, // somente dígitos
        val email: String
    )

    /** Registro interno com as credenciais; o hash não sai do repositório. */
    private data class EmpresaRecord(
        val nome: String,
        val cnpj: String,
        val email: String,
        val senhaHash: String
    ) {
        fun semCredenciais() = Empresa(nome, cnpj, email)
    }

    sealed class AuthResult {
        data class Sucesso(val empresa: Empresa) : AuthResult()
        data class Erro(val mensagem: String) : AuthResult()
    }

    private val empresas = mutableListOf(
        EmpresaRecord(
            nome = "Fazendas Reunidas Bela Vista S/A",
            cnpj = "12345678000195",
            email = "contato@belavista.agr.br",
            senhaHash = PasswordHasher.hash("GeoAlerta2026")
        )
    )

    var empresaLogada: Empresa? = null
        private set

    fun login(cnpj: String, senha: String): AuthResult {
        if (!InputValidator.cnpjValido(cnpj)) {
            return AuthResult.Erro("CNPJ inválido. Verifique os dígitos informados.")
        }

        val empresa = empresas.find { it.cnpj == InputValidator.limparCnpj(cnpj) }
        // Mensagem genérica: não revela se o erro foi no CNPJ ou na senha,
        // evitando enumeração de contas cadastradas.
        if (empresa == null || !PasswordHasher.verificar(senha, empresa.senhaHash)) {
            return AuthResult.Erro("CNPJ ou senha incorretos.")
        }

        val publica = empresa.semCredenciais()
        empresaLogada = publica
        return AuthResult.Sucesso(publica)
    }

    fun cadastrar(nome: String, cnpj: String, email: String, senha: String): AuthResult {
        if (!InputValidator.textoSeguro(nome)) {
            return AuthResult.Erro("Nome da empresa inválido: não use caracteres especiais como < > ' \" ;")
        }
        if (!InputValidator.cnpjValido(cnpj)) {
            return AuthResult.Erro("CNPJ inválido. Verifique os dígitos informados.")
        }
        if (!InputValidator.emailValido(email)) {
            return AuthResult.Erro("E-mail inválido.")
        }
        if (!InputValidator.senhaForte(senha)) {
            return AuthResult.Erro(InputValidator.mensagemSenhaFraca())
        }

        val cnpjLimpo = InputValidator.limparCnpj(cnpj)
        if (empresas.any { it.cnpj == cnpjLimpo }) {
            return AuthResult.Erro("Já existe uma empresa cadastrada com este CNPJ.")
        }

        val empresa = EmpresaRecord(
            nome = nome.trim(),
            cnpj = cnpjLimpo,
            email = email.trim(),
            senhaHash = PasswordHasher.hash(senha)
        )
        empresas.add(empresa)
        val publica = empresa.semCredenciais()
        empresaLogada = publica
        return AuthResult.Sucesso(publica)
    }

    fun logout() {
        empresaLogada = null
    }
}
