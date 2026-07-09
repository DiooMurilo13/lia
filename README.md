# Lia

Assistente de voz para Android. Acorda com a wake word **"Ei Lia"**, ouve o usuario
e age no proprio celular — tracar rota no Maps, ligar, enviar mensagem, tocar musica —
com foco em uso **hands-free** via intercomunicador.

> Projeto em fase inicial. Este repositorio ja contem o esqueleto do app Android.

## Stack

- **Kotlin nativo**, app single-module (`:app`), sem backend.
- **Gradle wrapper** (Gradle 8.13) + **AGP 8.7.3**.
- `compileSdk` / `targetSdk` = 34 (Android 14), `minSdk` = 26 (Android 8 — cobre as
  APIs de Bluetooth SCO usadas no uso hands-free).
- Sem framework de injecao de dependencia no MVP (ver ADR de fundacao).

## Arquitetura (costura)

O nucleo ja prevê os pontos de extensao dos spikes de voz, ainda como stubs:

- `Interpreter` — transforma o texto reconhecido em uma `Intencao`.
- `Tool` + `ToolRegistry` — contrato de acao executavel e o registro que mapeia
  `Intencao` -> `Tool`. Os spikes (tocar musica, tracar rota, ligar, etc.) plugam aqui.

Codigo em `app/src/main/kotlin/com/diomurilo/lia/`.

## Pre-requisitos

- JDK 17.
- Android SDK instalado. Copie o caminho no `local.properties` (nao versionado):

  ```properties
  sdk.dir=/caminho/para/Android/Sdk
  ```

## Como compilar

```bash
./gradlew assembleDebug
```

O APK sai em `app/build/outputs/apk/debug/app-debug.apk`.

## Rodar os testes (JVM, sem emulador)

```bash
./gradlew test
```

## Lint / analise estatica

```bash
./gradlew lint
```

## Instalar no celular

1. No aparelho, habilite **Opcoes do desenvolvedor** e **Instalar apps desconhecidos**
   (e a depuracao USB, se for instalar por cabo).
2. Com o aparelho conectado:

   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

O app "Lia" abre em uma tela unica de diagnostico (a interface e so status — a Lia e
voz-primeiro).

## Decisoes de arquitetura

Registradas em [`docs/adr/`](docs/adr/). A fundacao esta na
[ADR 0001](docs/adr/0001-fundacao-android.md).
