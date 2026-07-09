package com.diomurilo.lia.core

/**
 * Intencao reconhecida a partir da fala do usuario.
 *
 * E o resultado da etapa de interpretacao ([Interpreter]) e a chave usada pelo
 * [ToolRegistry] para resolver qual [Tool] executa a acao.
 *
 * @property nome identificador canonico da intencao (ex: "tocar_musica", "ligar").
 * @property parametros dados extraidos da fala (ex: "contato" -> "Maria").
 */
data class Intencao(
    val nome: String,
    val parametros: Map<String, String> = emptyMap(),
)
