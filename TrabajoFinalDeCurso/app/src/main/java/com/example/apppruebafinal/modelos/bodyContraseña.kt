package com.example.apppruebafinal.modelos

import com.google.gson.annotations.SerializedName

data class bodyContraseña(
    @SerializedName("token")
    val token: String,
    @SerializedName("generarContrasena")
    val contraseña: String
):java.io.Serializable
