package com.aplicacion.turntracking.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.aplicacion.turntracking.R
import com.aplicacion.turntracking.databinding.ActivityLoginMailBinding
import com.google.firebase.auth.FirebaseAuth

class LoginMailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUp()
    }

    private fun setUp() {
        binding.buttonSing.setOnClickListener {
            if (binding.editTextEmail.text.isNotEmpty() && binding.editTextPassword.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString()
                ).addOnCompleteListener{
                    if (it.isSuccessful){
                        it.result?.user?.email?.let { saveUser(it) }
                        it.result?.user?.email?.let { goToHome(it) }
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun saveUser(email: String){
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        pref.putString("email", email)
        pref.apply()
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario. Quizas el usuario ya existe")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goToHome(email: String){
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}
