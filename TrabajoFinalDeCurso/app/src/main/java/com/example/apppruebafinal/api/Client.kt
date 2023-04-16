package com.example.apppruebafinal.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://40609376.servicio-online.net/api/usuariosTable/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiClient: Service = retrofit.create(Service::class.java)


}