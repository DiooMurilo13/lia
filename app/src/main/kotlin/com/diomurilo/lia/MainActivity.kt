package com.diomurilo.lia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Tela unica de diagnostico/status.
 *
 * A Lia e voz-primeiro (uso hands-free via intercomunicador): a interface grafica
 * serve apenas para confirmar que o app subiu. Nenhum fluxo de produto depende dela.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
