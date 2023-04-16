package com.example.apppruebafinal.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.example.apppruebafinal.api.Client
import com.example.apppruebafinal.databinding.ActivityLoginBinding
import com.example.apppruebafinal.modelos.bodyContraseña
import com.example.apppruebafinal.preferences.sharedPreferences
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException
import java.util.*

class LoginActivity : AppCompatActivity() {
    lateinit var Binding : ActivityLoginBinding
    var email = ""
    var contraseña = ""
    val key = "4c89e9fb3a2a30bc1060a776c4590699"
    lateinit var prefs: sharedPreferences

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)
        prefs = sharedPreferences(this)
        if (isInternetConnected(this)) {
            mirarsesion()
            setListener()
        } else {
            finish()
            startActivity(Intent(this, NoConnection::class.java))
        }



    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun mirarsesion() {
        if (!prefs.cogerEmail().isNullOrEmpty() && !prefs.cogerContraseña().isNullOrEmpty()) {
            email = prefs.cogerEmail().toString();
            contraseña = prefs.cogerContraseña().toString()

            lifecycleScope.launch {
                val datos = Client.apiClient.getUsers(key, email)
                if (!datos.email.isNullOrEmpty()) {
                    if (datos.email == email) {
                        if (datos.contraseña == contraseña) {
                            startActivity(Intent(Binding.email.context, HomeActivity::class.java))
                            finish()
                        }
                    }
                }
            }

        }
    }

    private fun setListener() {
        Binding.registro.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        Binding.iniciarSesionbtn.setOnClickListener{
            recogerDatos()
        }
    }

    private fun recogerDatos() {
        email = Binding.email.text.toString().trim().lowercase(Locale.getDefault())
        contraseña = Binding.contraseA.text.toString().trim()

        if(email.isEmpty() || !email.contains("@")){
            Binding.email.error = "Email vacio o invalido"
            Binding.email.requestFocus()
            return
        }
        if(contraseña.isEmpty() || contraseña.length < 8){
            Binding.contraseA.error = "contraseña vacia o invalida, minimo 8 caracteres"
            Binding.contraseA.requestFocus()
            return
        }
        getUser()

    }

    private fun getUser(){
        lifecycleScope.launch{
            val datos = Client.apiClient.getUsers(key, email)
            val passwdGen = Client.apiClient.generatePassword(bodyContraseña(key,contraseña))
            if(datos.email == email){
                if(datos.contraseña == passwdGen.contraseña){
                    if(datos.Verificado == "0"){
                        startActivity(Intent(Binding.email.context, VerificacionActivity::class.java).apply {
                            putExtra("ID", datos.id)
                            putExtra("email", datos.email)
                            putExtra("password", datos.contraseña)
                        })
                        finish()

                    }
                    else{
                        Toast.makeText(Binding.email.context, "Sesion iniciada", Toast.LENGTH_LONG).show()
                        prefs.guardarDatos(email, passwdGen.contraseña, datos.id)
                        prefs.guardarMegusta(datos.megusta)
                        startActivity(Intent(Binding.email.context, HomeActivity::class.java))

                        finish()
                    }

                }
                else{
                    Toast.makeText(Binding.email.context, "Email o contraseña erronea", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(Binding.email.context, "Email o contraseña erronea", Toast.LENGTH_LONG).show()
            }

        }

    }



}