package com.example.apppruebafinal.modelos

import com.google.gson.annotations.SerializedName

data class registroUsuario(

    val token: String,
    val email : String,
    @SerializedName("contrasena")
    val contraseña: String,
    val nombre_completo: String,
    val direccion: String,
    val cp: String,
    val provincia: String,
    val telefono: String


):java.io.Serializable
