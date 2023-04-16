package com.example.apppruebafinal.api.apiRopa

import com.example.apppruebafinal.modelos.modeloRopa.ropaPadreModel
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.example.apppruebafinal.modelos.usuario
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("ropa")
    suspend fun getClothes(@Query("token") token:String ,@Query("page") page:String): ropaPadreModel

    @GET("ropa")
    suspend fun getClothesByType(@Query("token") token:String,@Query("page") page:String ,@Query("Tipo") tipo:String, @Query("Color") Color:String, @Query("Talla") talla:String, @Query("Sexo") sexo:String): ropaPadreModel


    @GET("ropa")
    suspend fun getClothesId(@Query("token") token:String ,@Query("id") id:String): ropamodel
}