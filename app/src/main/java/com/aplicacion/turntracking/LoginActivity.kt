package com.aplicacion.turntracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) //le seteo nuevamente el estilo de la aplicaciomn porqie al iniciar tiene el style del splash
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
