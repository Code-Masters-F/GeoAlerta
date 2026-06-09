package com.geoalerta.app.auth

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hash de senhas com PBKDF2 + salt aleatório.
 *
 * A senha nunca é armazenada em texto puro: guardamos apenas
 * `algoritmo$iteracoes$salt$hash` (salt e hash em Base64). O salt aleatório
 * garante que duas senhas iguais gerem hashes diferentes, e o número alto de
 * iterações torna ataques de força bruta muito mais caros.
 */
object PasswordHasher {

    private const val ITERACOES = 60_000
    private const val TAMANHO_CHAVE_BITS = 256
    private const val TAMANHO_SALT_BYTES = 16

    // PBKDF2WithHmacSHA256 só existe a partir do Android 8 (API 26);
    // em versões anteriores (minSdk 24) caímos para HmacSHA1.
    private val algoritmo: String = try {
        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        "PBKDF2WithHmacSHA256"
    } catch (e: NoSuchAlgorithmException) {
        "PBKDF2WithHmacSHA1"
    }

    /** Gera o hash de uma senha em texto puro, com salt novo e aleatório. */
    fun hash(senha: String): String {
        val salt = ByteArray(TAMANHO_SALT_BYTES).also { SecureRandom().nextBytes(it) }
        val derivada = derivar(senha, salt, ITERACOES, algoritmo)
        return listOf(
            algoritmo,
            ITERACOES.toString(),
            Base64.encodeToString(salt, Base64.NO_WRAP),
            Base64.encodeToString(derivada, Base64.NO_WRAP)
        ).joinToString(SEPARADOR)
    }

    /**
     * Verifica se a senha informada corresponde ao hash armazenado.
     * Usa [MessageDigest.isEqual] (comparação em tempo constante) para não
     * vazar, pelo tempo de resposta, quantos bytes do hash conferem.
     */
    fun verificar(senha: String, hashArmazenado: String): Boolean {
        val partes = hashArmazenado.split(SEPARADOR)
        if (partes.size != 4) return false
        val iteracoes = partes[1].toIntOrNull() ?: return false
        val salt = Base64.decode(partes[2], Base64.NO_WRAP)
        val esperado = Base64.decode(partes[3], Base64.NO_WRAP)
        val candidato = derivar(senha, salt, iteracoes, partes[0])
        return MessageDigest.isEqual(esperado, candidato)
    }

    private fun derivar(senha: String, salt: ByteArray, iteracoes: Int, algoritmo: String): ByteArray {
        val spec = PBEKeySpec(senha.toCharArray(), salt, iteracoes, TAMANHO_CHAVE_BITS)
        return try {
            SecretKeyFactory.getInstance(algoritmo).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private const val SEPARADOR = "$"
}
