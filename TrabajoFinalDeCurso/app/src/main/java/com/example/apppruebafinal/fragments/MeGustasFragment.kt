package com.example.apppruebafinal.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apppruebafinal.views.InfoActivity
import com.example.apppruebafinal.R
import com.example.apppruebafinal.adapters.adapterMegustas.megustaAdapter
import com.example.apppruebafinal.api.apiRopa.Client
import com.example.apppruebafinal.databinding.FragmentMeGustasBinding
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.example.apppruebafinal.preferences.sharedPreferences
import kotlinx.coroutines.launch


class MeGustasFragment : Fragment() {
    lateinit var Binding: FragmentMeGustasBinding
    lateinit var prefs: sharedPreferences
    val token = "4c89e9fb3a2a30bc1060a776c4590699"
    var list = mutableListOf<ropamodel>()
    lateinit var adapter: megustaAdapter
    var resume = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Binding = FragmentMeGustasBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.clear()
        prefs = sharedPreferences(Binding.imageView4.context)
        setter()
        listarRopa()
    }

    private fun setter() {
        Binding.imageView4.setImageResource(R.drawable.corazonrelleno)
    }

    @SuppressLint("SetTextI18n")
    private fun listarRopa() {
        var contador = 0
        Binding.vacioGusta.visibility = View.VISIBLE
        Binding.recylcerGusta.visibility = View.INVISIBLE
        if(prefs.cogerMegusta() != "null"){
            Binding.vacioGusta.text = "Cargando..."
            Binding.vacioGusta.text
            var lista = prefs.cogerMegusta()!!.split(",")
            var cantidadLista = lista.size -1
            for (i in lista){
                if(i.isNotEmpty()){
                    if(i != "null"){
                        lifecycleScope.launch {
                            val modelo = Client.apiClientRopa.getClothesId(token, i)
                            list.add(modelo)

                            contador += 1
                            if(contador == 0){
                                Binding.vacioGusta.visibility = View.VISIBLE
                                Binding.recylcerGusta.visibility = View.INVISIBLE
                            }
                            if(contador == cantidadLista){
                                traerRopa()
                                Binding.vacioGusta.visibility = View.INVISIBLE
                                Binding.recylcerGusta.visibility = View.VISIBLE
                            }
                        }
                    }


                }

            }
        }
        else{
            Binding.vacioGusta.text = "Parece que no le has echado el ojo a ningun articulo, aún estas a tiempo."
        }

    }

    private fun traerRopa() {
        val layoutmanager = GridLayoutManager(Binding.textView20.context, 2)
        Binding.recylcerGusta.layoutManager = layoutmanager
        adapter = megustaAdapter(list){ropamodel->info(ropamodel)}
        Binding.recylcerGusta.adapter = adapter
        Binding.recylcerGusta.adapter!!.notifyDataSetChanged()

    }

    private fun info(model: ropamodel) {
        resume = true
        startActivity(Intent(Binding.textView20.context, InfoActivity::class.java).apply {
            putExtra("MODELO", model)
        })

    }


    override fun onResume() {
        super.onResume()
        if(resume){
            list.clear()
            listarRopa()
            resume = false
        }
    }

}