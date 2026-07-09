package com.diomurilo.lia

import android.app.Application
import com.diomurilo.lia.core.ToolRegistry

/**
 * Application da Lia.
 *
 * Ponto unico de composicao do app (sem DI framework no MVP). Aqui vivem os
 * registros de longa duracao — por ora, apenas o [ToolRegistry], que os spikes
 * de acao vao popular.
 */
class LiaApplication : Application() {

    /** Registro global de ferramentas/acoes. Preenchido pelos spikes futuros. */
    val toolRegistry: ToolRegistry by lazy { ToolRegistry() }
}
