package com.example.apppruebafinal.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppruebafinal.R
import com.example.apppruebafinal.adapters.adapterSugerencias.SugerenciasAdapter
import com.example.apppruebafinal.api.apiRopa.Client
import com.example.apppruebafinal.databinding.ActivityInfoBinding
import com.example.apppruebafinal.modelos.MegustaUpdate
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.example.apppruebafinal.preferences.sharedPreferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class InfoActivity : AppCompatActivity() {
    lateinit var Binding: ActivityInfoBinding
    var imagen = 1
    lateinit var modelo: ropamodel
    lateinit var adapter: SugerenciasAdapter
    lateinit var prefs: sharedPreferences
    var list = mutableListOf<ropamodel>()
    var carrito = ""
    val key = "4c89e9fb3a2a30bc1060a776c4590699"
    var megusta = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityInfoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)
        prefs = sharedPreferences(this)
        carrito = prefs.cogerCarrito().toString()
        recogerMegustas()
        getDatos()
        pintarDatos()
        listener()
        MostrarSugerencias()
        pintarRopa()
    }

    private fun recogerMegustas() {
        lifecycleScope.launch{
            val datos = com.example.apppruebafinal.api.Client.apiClient.getUsers(key, prefs.cogerEmail().toString())
            megusta = datos.megusta.toString()
            prefs.guardarMegusta(megusta)

        }
    }

    private fun getDatos() {
        modelo = getSerializable(intent, "MODELO", ropamodel::class.java)
    }
    private fun megusta(modelo: ropamodel) {
        if(megusta.isNullOrEmpty()){
            megusta += ","+modelo.id
            Binding.CorazonInfo.setImageResource(R.drawable.corazonrelleno)


        }
        else{
            if(!megusta.contains(modelo.id)){
                megusta += ","+modelo.id
                Binding.CorazonInfo.setImageResource(R.drawable.corazonrelleno)


            }
            else{
                Binding.CorazonInfo.setImageResource(R.drawable.corazonvacio)

                if(megusta == "null,"+modelo.id){
                    megusta = "null"
                    prefs.guardarMegusta(megusta)
                }
                var megustalist = megusta.split(",")
                var megusta2 = ""
                var contador = 0
                for ( i in megustalist){
                    if(i != modelo.id && i.isNotEmpty()){
                        if(contador == 0){
                            megusta2 += "$i"
                            contador += 1
                        }
                        else{
                            megusta2 += ",$i"
                        }
                    }
                }
                if(megusta2 == ",null"){
                    megusta2 = "null"
                }
                megusta = megusta2


            }

        }

        if (megusta != null) {
            val megus = MegustaUpdate(key,prefs.cogerID().toString(),"true",megusta)
            lifecycleScope.launch{
                com.example.apppruebafinal.api.Client.apiClient.updateMegusta(megus)
            }
            prefs.guardarMegusta(megusta)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun pintarDatos() {

        Binding.Atras.setBackgroundResource(android.R.drawable.ic_menu_revert)
        Binding.ImagenSiguiente.setImageResource(android.R.drawable.ic_media_next)
        Binding.imagenAnterior.setImageResource(android.R.drawable.ic_media_previous)
        Binding.imageView.setBackgroundResource(android.R.drawable.divider_horizontal_bright)

        Picasso.get().load(modelo.Imagen1).into(Binding.ImagenCentro)
        imagen = 1
        Binding.imagenAnterior.visibility = View.INVISIBLE

        Binding.Nombre.text = modelo.nombreRopa
        Binding.Descripcion.text = modelo.Descripcion
        Binding.Alto.text = "Alto: "+modelo.Alto
        Binding.Ancho.text = "Ancho: "+modelo.Ancho
        Binding.TipoInfo.text = modelo.Tipo
        Binding.tallaInfo.text = "Talla: "+modelo.Talla

        if(carrito.contains(modelo.id)){
            Binding.AAdirAlCarritoInfo.visibility = View.INVISIBLE
            Binding.aAdidoInfo.visibility = View.VISIBLE
        }
        else{
            Binding.AAdirAlCarritoInfo.visibility = View.VISIBLE
            Binding.aAdidoInfo.visibility = View.INVISIBLE
        }




    }

    private fun listener() {
        Binding.Atras.setOnClickListener{
            finish()
        }
        Binding.imagenAnterior.setOnClickListener{
            if (imagen != 1){
                imagen -= 1
                cambiarImagen()
            }
        }
        Binding.ImagenSiguiente.setOnClickListener{
            if (imagen != 8){
                imagen += 1
                cambiarImagen()
            }
        }

        Binding.AAdirAlCarritoInfo.setOnClickListener{
            añadiralCarrito()
        }

        Binding.CorazonInfo.setOnClickListener{
            megusta(modelo)
        }

    }

    private fun añadiralCarrito() {
        var carrito = prefs.cogerCarrito()
        if(carrito.isNullOrEmpty()){
            carrito += modelo.id

        }
        else{
            if(!carrito.contains(modelo.id)){
                carrito += ","+modelo.id

            }

        }
        if (carrito != null) {
            prefs.guardarCarrito(carrito)
            Binding.aAdidoInfo.visibility = View.VISIBLE
            Binding.AAdirAlCarritoInfo.visibility = View.INVISIBLE
        }
    }

    private fun cambiarImagen() {
        if(imagen == 1){
            Picasso.get().load(modelo.Imagen1).into(Binding.ImagenCentro)
            Binding.imagenAnterior.visibility = View.INVISIBLE
            Binding.ImagenSiguiente.visibility = View.VISIBLE
        }

        if(imagen == 2){
            Picasso.get().load(modelo.Imagen2).into(Binding.ImagenCentro)
            Binding.imagenAnterior.visibility = View.VISIBLE
            if(modelo.Imagen3 == ""){
                Binding.ImagenSiguiente.visibility = View.INVISIBLE
            }
            else{
                Binding.ImagenSiguiente.visibility = View.VISIBLE

            }
        }

        if(imagen == 3 && modelo.Imagen3 != ""){
            Picasso.get().load(modelo.Imagen3).into(Binding.ImagenCentro)
            if(modelo.Imagen4 == ""){
                Binding.ImagenSiguiente.visibility = View.INVISIBLE
            }
            else{
                Binding.ImagenSiguiente.visibility = View.VISIBLE

            }
        }

        if(imagen == 4 && modelo.Imagen4 != ""){
            Picasso.get().load(modelo.Imagen4).into(Binding.ImagenCentro)
            if(modelo.Imagen5 == ""){
                Binding.ImagenSiguiente.visibility = View.INVISIBLE
            }
            else{
                Binding.ImagenSiguiente.visibility = View.VISIBLE

            }
        }

        if(imagen == 5 && modelo.Imagen5 != ""){
            Picasso.get().load(modelo.Imagen5).into(Binding.ImagenCentro)
            if(modelo.Imagen6 == ""){
                Binding.ImagenSiguiente.visibility = View.INVISIBLE
            }
            else{
                Binding.ImagenSiguiente.visibility = View.VISIBLE

            }
        }

        if(imagen == 6 && modelo.Imagen6 != ""){
            Picasso.get().load(modelo.Imagen6).into(Binding.ImagenCentro)
            if(modelo.Imagen7 == ""){
                Binding.ImagenSiguiente.visibility = View.INVISIBLE
            }
            else{
                Binding.ImagenSiguiente.visibility = View.VISIBLE

            }
        }

        if(imagen == 7 && modelo.Imagen7 != ""){
            Picasso.get().load(modelo.Imagen7).into(Binding.ImagenCentro)
            if(modelo.Imagen8 == ""){
                Binding.ImagenSiguiente.visibility = View.INVISIBLE
            }
            else{
                Binding.ImagenSiguiente.visibility = View.VISIBLE

            }
        }

        if(imagen == 8 && modelo.Imagen8 != ""){
            Picasso.get().load(modelo.Imagen8).into(Binding.ImagenCentro)
            Binding.ImagenSiguiente.visibility = View.INVISIBLE

        }



    }

    private fun pintarRopa() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        Binding.sugerenciasRecyler.layoutManager = layoutManager

        adapter = SugerenciasAdapter(list){ropamodel ->  MostrarOtro(ropamodel)}
        Binding.sugerenciasRecyler.adapter = adapter



        if(prefs.cogerMegusta().toString().contains(modelo.id)){
            Binding.CorazonInfo.setImageResource(R.drawable.corazonrelleno)

        }
        else{
            Binding.CorazonInfo.setImageResource(R.drawable.corazonvacio)
        }
    }

    private fun MostrarOtro(modelo: ropamodel) {
        this.modelo = modelo
        pintarDatos()
        pintarRopa()
        pintarDatos()
        MostrarSugerencias()
    }

    private fun MostrarSugerencias(){
        Binding.scrollViewInfo.post(Runnable {
            Binding.scrollViewInfo.scrollTo(0,0)
        })

        lifecycleScope.launch{
            val ropa = Client.apiClientRopa.getClothesByType(key,"1",modelo.Tipo,"","",modelo.Sexo)
            if(ropa.results > 0){
                adapter.list = ropa.hits.toMutableList()
                val itemToDelete = adapter.list.find { it.id == modelo.id }
                if(itemToDelete != null){
                    adapter.list.remove(itemToDelete)
                    adapter.notifyDataSetChanged()

                }
                adapter.notifyDataSetChanged()
                Binding.textoSugerencias.visibility = View.VISIBLE

            }
            else{
                adapter.list.clear()
                adapter.notifyDataSetChanged()
                Binding.textoSugerencias.visibility = View.GONE

            }


        }
    }

    private fun <T: java.io.Serializable?> getSerializable(intent: Intent,key: String,clase: Class<T>): T {
        return if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            intent.getSerializableExtra(key,clase)!!
        }
        else{
            intent.getSerializableExtra(key) as T

        }

    }

}