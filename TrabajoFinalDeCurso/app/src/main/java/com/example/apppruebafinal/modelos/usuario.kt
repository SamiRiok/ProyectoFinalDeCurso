package com.example.apppruebafinal.modelos

import com.google.gson.annotations.SerializedName

data class usuario(

    val id: String,
    val email: String,
    @SerializedName("contrasena")
    val contraseña: String,
    val Verificado: String,
    val megusta: String


    ):java.io.Serializable
