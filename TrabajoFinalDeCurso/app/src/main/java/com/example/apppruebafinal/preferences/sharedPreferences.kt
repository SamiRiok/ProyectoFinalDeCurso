package com.example.apppruebafinal.preferences

import android.content.Context

class sharedPreferences(c: Context) {
    var prefs = c.getSharedPreferences("Guardados", 0)
    var prefs2 = c.getSharedPreferences("Carrito", 0)
    var prefs3 = c.getSharedPreferences("MEGUSTA", 0)


    fun guardarDatos(Email: String, Contraseña: String, ID: String){
        prefs.edit().putString("Email", Email).apply()
        prefs.edit().putString("Contraseña", Contraseña).apply()
        prefs.edit().putString("ID", ID).apply()
    }

    fun guardarVerf(Codigo: String){
        prefs.edit().putString("Codigo", Codigo).apply()

    }
    fun cogerEmail(): String? {
        return prefs.getString("Email", null)

    }
    fun cogerID(): String? {
        return prefs.getString("ID", null)

    }
    fun cogerContraseña(): String?{
        return prefs.getString("Contraseña", null)
    }
    fun cogerCodigo(): String?{
        return prefs.getString("Codigo", null)
    }

    fun borrarPrefs(){
        prefs.edit().clear().apply()
    }

    fun guardarCarrito(carrito: String){
        prefs2.edit().putString("CARRO", carrito).apply()
    }

    fun cogerCarrito(): String? {
        return prefs2.getString("CARRO", "")
    }
    fun borrarCarrito(){
        prefs2.edit().clear().apply()
    }

    fun guardarMegusta(carrito: String){
        prefs3.edit().putString("GUSTA", carrito).apply()
    }

    fun cogerMegusta(): String? {
        return prefs3.getString("GUSTA", "")
    }

    fun GuardarCantidades(cantidades: String){
        prefs.edit().putString("CANTIDAD", cantidades).apply()
    }

    fun cogerCantidades(): String?{
        return prefs.getString("CANTIDAD", "")
    }

    fun GuardarSugerencias(cantidades: String){
        prefs.edit().putString("SUGERENCIAS", cantidades).apply()
    }

    fun cogerSugerencias(): String?{
        return prefs.getString("SUGERENCIAS", "")
    }

    fun GuardarSugerencias2(cantidades: String){
        prefs.edit().putString("SUGERENCIAS2", cantidades).apply()
    }

    fun cogerSugerencias2(): String?{
        return prefs.getString("SUGERENCIAS2", "")
    }



}