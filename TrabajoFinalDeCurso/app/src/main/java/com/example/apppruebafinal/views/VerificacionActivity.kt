package com.example.apppruebafinal.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.apppruebafinal.api.Client
import com.example.apppruebafinal.databinding.ActivityVerificacionBinding
import com.example.apppruebafinal.modelos.GenerarNuevoCodigo
import com.example.apppruebafinal.modelos.verificadoUpdate
import com.example.apppruebafinal.preferences.sharedPreferences
import kotlinx.coroutines.launch

class VerificacionActivity : AppCompatActivity() {
    lateinit var Binding: ActivityVerificacionBinding
    lateinit var prefs: sharedPreferences
    val key = "4c89e9fb3a2a30bc1060a776c4590699"
    var codigo = ""
    var poderReenviar = true
    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityVerificacionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)
        prefs = sharedPreferences(this)
        listner()

    }

    private fun listner() {
        Binding.VerificarButton.setOnClickListener{
            RecogerCodigo()
        }
        Binding.Reenviar.setOnClickListener{
            if(poderReenviar){
                ReenviarCodigo()
            }
        }
    }

    private fun ReenviarCodigo() {
        val datos = intent.extras
        val email = datos!!.getString("email")
        lifecycleScope.launch {
            val datos = Client.apiClient.reenviarCodigo(GenerarNuevoCodigo(key,email.toString()))
            if(!datos.codigo.isNullOrEmpty()){
                val codigoCod = datos.codigo.toByteArray().joinToString("") { "%02x".format(it) }
                poderReenviar = false
                prefs.guardarVerf(codigoCod)
                Toast.makeText(Binding.Reenviar.context, "Codigo reenviado, revisa tu bandeja de entrada", Toast.LENGTH_LONG).show()
                Binding.Reenviar.text = "Ya has enviado el codigo"
            }
        }
    }

    private fun RecogerCodigo() {
        codigo = Binding.codigoVerificacion.text.toString()

        val codigoCod = codigo.toByteArray().joinToString("") { "%02x".format(it) }

        if(codigoCod != prefs.cogerCodigo().toString()){
            Binding.codigoVerificacion.error = "El codigo introducido no es correcto"
            Binding.codigoVerificacion.requestFocus()
            return
        }
        actualizarUsuario()
    }

    private fun actualizarUsuario() {
        val datos = intent.extras
        val id = datos!!.getString("ID")
        val email = datos!!.getString("email")
        val contraseña = datos!!.getString("password")
        if(id.isNullOrEmpty() || email.isNullOrEmpty() || contraseña.isNullOrEmpty()){
            Toast.makeText(this, "error interno, vuelva a intentarlo", Toast.LENGTH_LONG).show()
            finish()
        }
        lifecycleScope.launch {
            Client.apiClient.updateVerificado(verificadoUpdate(key,id.toString(),"true","1"))
            startActivity(Intent(Binding.codigoVerificacion.context, HomeActivity::class.java))
            prefs.guardarDatos(email.toString(), contraseña.toString(), id.toString())
            finish()
        }
    }
}