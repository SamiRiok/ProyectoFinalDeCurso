package com.example.apppruebafinal.api.apiRopa

import com.example.apppruebafinal.api.apiRopa.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://40609376.servicio-online.net/api/ropaTable/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiClientRopa: Service = retrofit.create(Service::class.java)

}