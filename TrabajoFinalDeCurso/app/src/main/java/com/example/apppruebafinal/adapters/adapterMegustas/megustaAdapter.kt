package com.example.apppruebafinal.adapters.adapterMegustas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.apppruebafinal.R
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel



class megustaAdapter(val list: MutableList<ropamodel>, val Info: (ropamodel)->Unit) : Adapter<MegustaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MegustaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sugrenciascard, parent, false)
        return MegustaViewHolder(v)
    }

    override fun onBindViewHolder(holder: MegustaViewHolder, position: Int) {
        holder.render(list[position], Info)
    }

    override fun getItemCount() = list.size

    fun getItem(position: Int): ropamodel {
        return list[position]
    }

}