package com.diomurilo.lia.core

/**
 * Acao executavel da Lia (uma "ferramenta").
 *
 * Cada spike futuro (tocar musica, tracar rota no Maps, ligar, enviar mensagem)
 * pluga aqui uma implementacao. A execucao concreta costuma disparar um Intent
 * do Android ou uma media session — preferidos a Acessibilidade.
 */
interface Tool {

    /**
     * Nome da intencao que esta ferramenta atende. O [ToolRegistry] usa este
     * valor para mapear [Intencao.nome] -> [Tool].
     */
    val nomeIntencao: String

    /**
     * Executa a acao para a [intencao] resolvida e devolve o resultado.
     */
    fun executar(intencao: Intencao): ResultadoTool
}

/**
 * Resultado da execucao de uma [Tool].
 *
 * @property sucesso indica se a acao foi concluida.
 * @property mensagem texto curto para retorno por voz ao usuario (opcional).
 */
data class ResultadoTool(
    val sucesso: Boolean,
    val mensagem: String? = null,
)
