# ADR 0001 — Fundacao do app Android

- **Status:** Aceita
- **Data:** 2026-07-09
- **Contexto da issue:** Base do app Android (esqueleto para os spikes)

## Contexto

A Lia e uma assistente de voz que roda em um Android pessoal (aparelho alvo:
POCO X6 Pro, Android 14+), voz-primeiro e hands-free. Antes dos spikes de voz
(reconhecimento de fala, tocar musica, tracar rota, ligar) e preciso uma fundacao
minima e limpa: modulo do app, build reprodutivel, manifesto, uma tela de status
e a costura de arquitetura por onde os spikes vao plugar. Esta base e dependencia
comum das provas de conceito seguintes.

## Decisoes

### 1. Kotlin nativo, app single-module (`:app`)

App Android nativo em Kotlin, um unico modulo `:app`. Sem backend proprio no
escopo desta fundacao.

- **Por que:** o produto age no proprio aparelho via APIs do Android (Intents,
  media session, telefonia). Nativo da acesso direto e estavel a essas APIs. Um
  unico modulo mantem o build simples enquanto o app e pequeno; a modularizacao
  pode vir depois, se a complexidade justificar.
- **Rejeitado:** multi-modulo desde o inicio (complexidade sem retorno neste
  tamanho); cross-platform / React Native / Flutter (camada extra sobre APIs de
  voz, telefonia e Bluetooth SCO que sao justamente o coracao do produto).

### 2. SDK: `compileSdk`/`targetSdk` = 34, `minSdk` = 26

- **`compileSdk` = 34 e `targetSdk` = 34 (Android 14):** o aparelho alvo e
  Android 14+; compilar e mirar na 34 garante comportamento previsivel no
  aparelho e acesso as APIs atuais.
- **`minSdk` = 26 (Android 8):** cobre o uso pessoal e, principalmente, as APIs
  de audio/Bluetooth SCO (modo de conversa/intercomunicador) que a Lia usa; abrir
  demais o `minSdk` traria custo de compatibilidade sem ganho real para um app de
  uso pessoal.
- **Rejeitado:** `minSdk` mais baixo (ex: 21/23) — sem base de usuarios legada a
  atender e evita workarounds de APIs antigas.

### 3. Build: Gradle wrapper (8.13) + AGP 8.7.3 + Kotlin 2.0.21

- **Por que:** o **wrapper** garante build reprodutivel sem depender de Gradle
  global na maquina (o ambiente de dev nao tem Gradle instalado). AGP 8.7.x e
  compativel com `compileSdk` 34 e com o Gradle 8.13; a `buildToolsVersion`
  padrao do AGP 8.7 (35.0.0) ja esta instalada, evitando download extra.

### 4. Sem framework de injecao de dependencia no MVP

A composicao e feita "na mao" na `LiaApplication` (por ex., a instancia do
`ToolRegistry`).

- **Por que:** o grafo de dependencias e minusculo. Um framework (Hilt/Koin)
  adicionaria plugins, geracao de codigo e curva de setup sem retorno neste
  estagio. Simplicidade primeiro.
- **Rejeitado (por ora):** Hilt/Dagger e Koin. Reavaliar quando o numero de
  dependencias de longa duracao crescer a ponto de a fiacao manual doer.

### 5. Estrategia de teste: unit JVM agora; instrumentado/emulador fora

- **Agora:** testes unitarios em JVM (JUnit), que rodam sem emulador. A logica de
  costura (`ToolRegistry`) ja tem cobertura real (registrar, resolver, executar,
  ausencia de ferramenta, sobrescrita por intencao).
- **Fora do escopo:** testes instrumentados/em emulador. A validacao de campo
  (app abrir e reconhecer voz no aparelho) e feita no proprio POCO X6 Pro pelo
  humano; manter um emulador Android neste ambiente WSL nao compensa.
- **Rejeitado:** exigir testes instrumentados na CI desta fase (custo de
  infra/emulador desproporcional ao retorno agora).

### 6. Acoes via Intents / media session antes de Acessibilidade

Fica registrado como diretriz de arquitetura para os spikes: preferir **Intents**
e **media session** (contratos estaveis do Android) para executar acoes; recorrer
a **Acessibilidade / Shizuku** apenas quando nenhum Intent cobrir o caso.

- **Por que:** Intents/media session sao estaveis entre versoes e apps; a
  Acessibilidade e fragil (quebra com mudanca de layout/versao) e tem implicacoes
  de permissao e privacidade. O `Tool`/`ToolRegistry` foi desenhado para que cada
  acao encapsule sua estrategia de execucao.

### 7. Lint padrao do AGP habilitado; sem ktlint no MVP

- **Por que:** o `lint` do AGP ja cobre os problemas relevantes de Android e roda
  no proprio build (`abortOnError = true`). Adicionar ktlint agora traria mais um
  plugin/tarefa e ajuste de regras sem ganho proporcional neste tamanho de codigo.
- **Rejeitado (por ora):** ktlint/detekt como gate. Reavaliar se o time crescer.

## Consequencias

- Build reprodutivel via `./gradlew assembleDebug` sem Gradle global.
- Base pronta para os spikes plugarem `Tool`s no `ToolRegistry` e um `Interpreter`
  concreto, sem retrabalho de fundacao.
- A tela e apenas diagnostico; nenhum fluxo de produto depende de UI.
- Divida tecnica assumida e consciente: sem DI framework, sem modularizacao, sem
  testes instrumentados — todas reavaliaveis quando a complexidade justificar.
