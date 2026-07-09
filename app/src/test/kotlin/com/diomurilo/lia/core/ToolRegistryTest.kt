package com.diomurilo.lia.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Testes do [ToolRegistry] — rodam na JVM, sem emulador.
 *
 * Cobrem o contrato de costura das acoes: registrar, resolver e executar
 * ferramentas por intencao.
 */
class ToolRegistryTest {

    /** Ferramenta de teste que apenas ecoa a intencao recebida. */
    private class ToolFake(override val nomeIntencao: String) : Tool {
        var vezesExecutada: Int = 0
            private set

        override fun executar(intencao: Intencao): ResultadoTool {
            vezesExecutada++
            return ResultadoTool(sucesso = true, mensagem = "ok:${intencao.nome}")
        }
    }

    @Test
    fun `resolve a ferramenta registrada para a intencao`() {
        val registry = ToolRegistry()
        val tool = ToolFake(nomeIntencao = "tocar_musica")
        registry.registrar(tool)

        val resolvida = registry.resolver(Intencao(nome = "tocar_musica"))

        assertSame("deve devolver a mesma instancia registrada", tool, resolvida)
        assertEquals(1, registry.quantidade)
    }

    @Test
    fun `retorna null quando nenhuma ferramenta atende a intencao`() {
        val registry = ToolRegistry()

        val resolvida = registry.resolver(Intencao(nome = "intencao_desconhecida"))

        assertNull(resolvida)
        assertEquals(0, registry.quantidade)
    }

    @Test
    fun `executar dispara a ferramenta certa e devolve o resultado`() {
        val registry = ToolRegistry()
        val tool = ToolFake(nomeIntencao = "ligar")
        registry.registrar(tool)

        val resultado = registry.executar(Intencao(nome = "ligar", parametros = mapOf("contato" to "Maria")))

        assertTrue(resultado!!.sucesso)
        assertEquals("ok:ligar", resultado.mensagem)
        assertEquals(1, tool.vezesExecutada)
    }

    @Test
    fun `executar retorna null sem efeito quando nao ha ferramenta`() {
        val registry = ToolRegistry()

        val resultado = registry.executar(Intencao(nome = "inexistente"))

        assertNull(resultado)
    }

    @Test
    fun `ultima ferramenta registrada para a mesma intencao vence`() {
        val registry = ToolRegistry()
        val primeira = ToolFake(nomeIntencao = "navegar")
        val segunda = ToolFake(nomeIntencao = "navegar")

        registry.registrar(primeira)
        registry.registrar(segunda)

        assertSame(segunda, registry.resolver(Intencao(nome = "navegar")))
        assertEquals("nao deve duplicar a chave de intencao", 1, registry.quantidade)
    }
}
