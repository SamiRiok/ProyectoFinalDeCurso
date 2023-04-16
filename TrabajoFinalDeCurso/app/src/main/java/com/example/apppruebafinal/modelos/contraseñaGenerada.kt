package com.example.apppruebafinal.modelos

import com.google.gson.annotations.SerializedName

data class contraseñaGenerada(
    @SerializedName("contrasenaGenerada")
    val contraseña: String
):java.io.Serializable
