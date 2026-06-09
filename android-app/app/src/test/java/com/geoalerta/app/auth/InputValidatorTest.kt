package com.geoalerta.app.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `cnpj valido com mascara passa`() {
        assertTrue(InputValidator.cnpjValido("12.345.678/0001-95"))
    }

    @Test
    fun `cnpj com digito verificador errado falha`() {
        assertFalse(InputValidator.cnpjValido("12.345.678/0001-96"))
    }

    @Test
    fun `cnpj com sequencia repetida falha`() {
        assertFalse(InputValidator.cnpjValido("11.111.111/1111-11"))
    }

    @Test
    fun `cnpj curto falha`() {
        assertFalse(InputValidator.cnpjValido("123"))
    }

    @Test
    fun `email valido passa e invalido falha`() {
        assertTrue(InputValidator.emailValido("contato@empresa.com.br"))
        assertFalse(InputValidator.emailValido("contato@empresa"))
        assertFalse(InputValidator.emailValido("sem-arroba.com"))
    }

    @Test
    fun `senha forte exige tamanho maiuscula minuscula e numero`() {
        assertTrue(InputValidator.senhaForte("GeoAlerta2026"))
        assertFalse(InputValidator.senhaForte("curta1A"))
        assertFalse(InputValidator.senhaForte("semnumeroAa"))
        assertFalse(InputValidator.senhaForte("semmaiuscula1"))
    }

    @Test
    fun `texto com payload de injecao e rejeitado`() {
        assertFalse(InputValidator.textoSeguro("<script>alert(1)</script>"))
        assertFalse(InputValidator.textoSeguro("Empresa'; DROP TABLE empresa;--"))
        assertTrue(InputValidator.textoSeguro("Fazendas Reunidas Bela Vista S/A"))
    }
}
