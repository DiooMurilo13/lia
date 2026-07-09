package com.diomurilo.lia.core

/**
 * Registro que mapeia uma [Intencao] para a [Tool] capaz de executa-la.
 *
 * E o ponto de costura das acoes: os spikes futuros registram suas ferramentas
 * aqui (via [registrar]) e o orquestrador de voz resolve a acao com [resolver].
 *
 * Mantido deliberadamente simples (sem DI framework no MVP): um mapa em memoria
 * indexado por [Tool.nomeIntencao].
 */
class ToolRegistry {

    private val ferramentasPorIntencao = mutableMapOf<String, Tool>()

    /**
     * Registra uma [tool]. Se ja existir uma ferramenta para o mesmo
     * `nomeIntencao`, ela e substituida (ultima registrada vence).
     */
    fun registrar(tool: Tool) {
        ferramentasPorIntencao[tool.nomeIntencao] = tool
    }

    /**
     * Resolve a [Tool] responsavel pela [intencao], ou `null` se nenhuma
     * ferramenta foi registrada para ela.
     */
    fun resolver(intencao: Intencao): Tool? = ferramentasPorIntencao[intencao.nome]

    /**
     * Resolve e executa a ferramenta correspondente a [intencao].
     *
     * @return o [ResultadoTool] da execucao, ou `null` quando nao ha ferramenta
     * registrada para a intencao.
     */
    fun executar(intencao: Intencao): ResultadoTool? = resolver(intencao)?.executar(intencao)

    /** Quantidade de ferramentas atualmente registradas. */
    val quantidade: Int
        get() = ferramentasPorIntencao.size
}
