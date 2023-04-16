package com.example.apppruebafinal.adapters.adapterRopa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.apppruebafinal.R

import com.example.apppruebafinal.modelos.modeloRopa.ropamodel

class ropaAdapter(var list: MutableList<ropamodel>, private val onAddCart:(ropamodel)-> Unit, private val info: (ropamodel) -> Unit, private val megusta: (ropamodel) -> Unit): Adapter<ropaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ropaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ropacard, parent, false)
        return ropaViewHolder(v)
    }

    override fun onBindViewHolder(holder: ropaViewHolder, position: Int) {
        holder.render(list[position], onAddCart, info, megusta)
    }

    override fun getItemCount() = list.size
}