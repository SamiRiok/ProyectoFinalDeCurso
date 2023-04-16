package com.example.apppruebafinal.api

import com.example.apppruebafinal.modelos.*
import retrofit2.http.*

interface Service {

    @GET("usuarios")
    suspend fun getUsers(@Query("token") token:String, @Query("email") email:String):usuario

    @Headers("Content-Type: application/json")
    @POST("usuarios")
    suspend fun generatePassword(@Body body: bodyContraseña) : contraseñaGenerada

    @Headers("Content-Type: application/json")
    @POST("usuarios")
    suspend fun registerUser(@Body body: registroUsuario) : codigoVerificacion

    @Headers("Content-Type: application/json")
    @POST("usuarios")
    suspend fun reenviarCodigo(@Body body: GenerarNuevoCodigo) : codigoVerificacion

    @Headers("Content-Type: application/json")
    @POST("usuarios")
    suspend fun updateVerificado(@Body body: verificadoUpdate)

    @Headers("Content-Type: application/json")
    @POST("usuarios")
    suspend fun updateMegusta(@Body body: MegustaUpdate)

}