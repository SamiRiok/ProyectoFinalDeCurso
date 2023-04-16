package com.example.apppruebafinal.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.apppruebafinal.api.Client
import com.example.apppruebafinal.databinding.ActivityRegisterBinding
import com.example.apppruebafinal.modelos.registroUsuario
import com.example.apppruebafinal.preferences.sharedPreferences
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    lateinit var Binding: ActivityRegisterBinding
    var existe = 2
    var registro = true

    //VARIABLES DE REGISTRO
    val key = "4c89e9fb3a2a30bc1060a776c4590699"
    var email = ""
    var contraseña = ""
    var contraseñaConf = ""
    var nombre = ""
    var telefono = 0
    var realizarRegistro = true

    lateinit var prefs: sharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)
        prefs = sharedPreferences(this)
        listener()
    }

    private fun listener() {
        Binding.CompletarRegistro.setOnClickListener{

            if(realizarRegistro){

                recogerDatos()
            }


        }
    }

    private fun recogerDatos() {
        email = Binding.emaiLReg.text.toString().trim()
        contraseña = Binding.ContraseAReg.text.toString().trim()
        contraseñaConf = Binding.RepetirContraseAReg.text.toString().trim()
        nombre = Binding.NombreReg.text.toString()



        if(email.isEmpty() || !email.contains("@")){
            Binding.emaiLReg.error = "Email vacio o invalido"
            Binding.emaiLReg.requestFocus()
            return
        }
        if(contraseña.isEmpty() || contraseña.length < 8){
            Binding.ContraseAReg.error = "contraseña vacia o invalida, minimo 8 caracteres"
            Binding.ContraseAReg.requestFocus()
            return
        }
        if(contraseñaConf != contraseña){
            Binding.RepetirContraseAReg.error = "Las Contraseñas no coinciden"
            Binding.RepetirContraseAReg.requestFocus()
            return
        }
        if(nombre.isEmpty()){
            Binding.NombreReg.error = "Nombre vacio"
            Binding.NombreReg.requestFocus()
            return
        }
        if(Binding.TelefonoReg.length() < 9){
            Binding.TelefonoReg.error = "Telefono Invalido"
            Binding.TelefonoReg.requestFocus()
            return
        }





        telefono = Binding.TelefonoReg.text.toString().trim().toInt()
        realizarRegistro = false
        Binding.CompletarRegistro.text = "Registando..."
        procederRegistro()
    }


    private fun procederRegistro() {

        lifecycleScope.launch {
            val datos = Client.apiClient.getUsers(key, email)
            if (datos.email == email) {
                Binding.emaiLReg.error = "Este email ya existe"
                Binding.emaiLReg.requestFocus()
                realizarRegistro = true
                Binding.CompletarRegistro.text = "Finalizar"

            }
            else{
                val datos = Client.apiClient.registerUser(registroUsuario(key,email, contraseña, nombre,"sin rellenar","00000","sin rellenar", telefono.toString()))
                registro = false
                val codigoCod = datos.codigo.toByteArray().joinToString("") { "%02x".format(it) }
                prefs.guardarVerf(codigoCod)
                if(prefs.cogerCodigo().toString().length > 4){
                    Toast.makeText(Binding.TelefonoReg.context, "Registrado con exito, ahora puedes iniciar Sesion", Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    realizarRegistro = true
                    Toast.makeText(Binding.TelefonoReg.context, "Error interno, vuelva a intentarlo", Toast.LENGTH_LONG).show()
                    Binding.CompletarRegistro.text = "Finalizar"
                    return@launch

                }

            }
        }


    }

}