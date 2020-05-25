package com.aplicacion.turntracking.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.aplicacion.turntracking.R
import com.aplicacion.turntracking.analytics.TrackingAnalyticsHandler
import com.aplicacion.turntracking.analytics.TrackingAnalyticsLogger
import com.aplicacion.turntracking.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var analytics: TrackingAnalyticsLogger
    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) //le seteo nuevamente el estilo de la aplicaciomn porqie al iniciar tiene el style del splash
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        logAnalytics()

        if (userLoged() != null){
                goToHome()
        }
    }

    private fun setUpView(){
        binding.buttonMail.setOnClickListener{
            val loginIntent = Intent(this, LoginMailActivity::class.java)
            startActivity(loginIntent)
        }

        binding.buttonGoogle.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut() //nos desloguamos por las dudas que tenga ams de 1 cuenta
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    private fun userLoged() = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).getString("email", null)

    private fun goToHome(){
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }

    private fun logAnalytics(){
        //pueden que demoren 24 horas la entrada de los eventos en google
        analytics = TrackingAnalyticsHandler()
        analytics.logNewUser(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Ya accedimos a la cuenta de googler del usuario, ahora lo logueamos en firebase
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            saveUser(account.email.toString())
                            goToHome()
                        }else{
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException){
                showAlert()
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario. Quizas el usuario ya existe.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveUser(email: String){
        val pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        pref.putString("email", email)
        pref.apply()
    }
}
