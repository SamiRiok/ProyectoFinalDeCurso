package com.example.apppruebafinal.adapters.adapterSugerencias

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.apppruebafinal.R

import com.example.apppruebafinal.modelos.modeloRopa.ropamodel

class SugerenciasAdapter(var list: MutableList<ropamodel>, private val onAddCart:(ropamodel)-> Unit): Adapter<SugerenciasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugerenciasViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sugrenciascard, parent, false)
        return SugerenciasViewHolder(v)
    }

    override fun onBindViewHolder(holder: SugerenciasViewHolder, position: Int) {
        holder.render(list[position], onAddCart)
    }

    override fun getItemCount() = list.size
}