package com.geoalerta.app.auth

/**
 * Validação e sanitização de entrada do usuário (login e cadastro).
 *
 * Toda entrada é validada antes de chegar ao [AuthRepository]: formato e
 * dígitos verificadores do CNPJ, formato de e-mail, força mínima da senha e
 * bloqueio de caracteres usados em ataques de injeção (SQLi/XSS), como
 * `<`, `>`, `'`, `"` e `;`.
 */
object InputValidator {

    private val REGEX_EMAIL = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // Caracteres típicos de payloads de SQL Injection e XSS.
    private val CARACTERES_PERIGOSOS = Regex("[<>'\";`\\\\]|--")

    const val TAMANHO_MAX_CAMPO = 120

    /** Remove a máscara do CNPJ, mantendo apenas os dígitos. */
    fun limparCnpj(cnpj: String): String = cnpj.filter { it.isDigit() }

    /**
     * Valida um CNPJ: 14 dígitos, não pode ser sequência repetida e os dois
     * dígitos verificadores precisam conferir (algoritmo oficial, módulo 11).
     */
    fun cnpjValido(cnpj: String): Boolean {
        val digitos = limparCnpj(cnpj)
        if (digitos.length != 14) return false
        if (digitos.all { it == digitos[0] }) return false

        val numeros = digitos.map { it - '0' }
        val pesos1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val pesos2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        fun digitoVerificador(quantidade: Int, pesos: IntArray): Int {
            val soma = (0 until quantidade).sumOf { numeros[it] * pesos[it] }
            val resto = soma % 11
            return if (resto < 2) 0 else 11 - resto
        }

        return numeros[12] == digitoVerificador(12, pesos1) &&
            numeros[13] == digitoVerificador(13, pesos2)
    }

    fun emailValido(email: String): Boolean =
        email.length <= TAMANHO_MAX_CAMPO && REGEX_EMAIL.matches(email.trim())

    /**
     * Política mínima de senha: 8+ caracteres, com letra maiúscula,
     * minúscula e número.
     */
    fun senhaForte(senha: String): Boolean =
        senha.length >= 8 &&
            senha.any { it.isUpperCase() } &&
            senha.any { it.isLowerCase() } &&
            senha.any { it.isDigit() }

    fun mensagemSenhaFraca(): String =
        "A senha deve ter no mínimo 8 caracteres, com letra maiúscula, minúscula e número."

    /**
     * Texto livre (ex.: nome da empresa) é considerado seguro se não estiver
     * vazio, respeitar o tamanho máximo e não conter caracteres perigosos.
     */
    fun textoSeguro(texto: String): Boolean =
        texto.isNotBlank() &&
            texto.length <= TAMANHO_MAX_CAMPO &&
            !CARACTERES_PERIGOSOS.containsMatchIn(texto)
}
