package com.example.apppruebafinal.adapters.adapterCarrito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.apppruebafinal.R
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel



class carritoAdapter(val list: MutableList<ropamodel>, val Info: (ropamodel)->Unit, val CambioCantidad: (Int, Int, String, String) -> Unit) : Adapter<carritoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): carritoViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.carritocard, parent, false)
        return carritoViewHolder(v)
    }

    override fun onBindViewHolder(holder: carritoViewHolder, position: Int) {
        holder.render(list[position], Info, CambioCantidad)
    }

    override fun getItemCount() = list.size

    fun getItem(position: Int): ropamodel {
        return list[position]
    }


}