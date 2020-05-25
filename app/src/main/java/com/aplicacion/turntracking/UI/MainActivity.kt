package com.aplicacion.turntracking.UI

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplicacion.turntracking.R
import com.aplicacion.turntracking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) //le seteo nuevamente el estilo de la aplicaciomn porqie al iniciar tiene el style del splash
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.TextHello.text = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString("email", null)

    }
}
