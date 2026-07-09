package com.diomurilo.lia.core

/**
 * Contrato que transforma um texto reconhecido (fala -> texto) em uma [Intencao].
 *
 * Ponto de extensao previsto na fundacao: as implementacoes concretas (heuristica
 * local, LLM via API auxiliar, etc.) chegam nos spikes seguintes. Aqui fica apenas
 * o contrato para que o restante da arquitetura ja consiga se costurar.
 */
interface Interpreter {

    /**
     * Interpreta o [texto] reconhecido e devolve a [Intencao] correspondente,
     * ou `null` quando nada foi entendido.
     */
    fun interpretar(texto: String): Intencao?
}
