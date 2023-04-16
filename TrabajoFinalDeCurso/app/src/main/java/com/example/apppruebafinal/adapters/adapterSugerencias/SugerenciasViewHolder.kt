package com.example.apppruebafinal.adapters.adapterSugerencias

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apppruebafinal.databinding.SugrenciascardBinding
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.squareup.picasso.Picasso

class SugerenciasViewHolder(v: View): ViewHolder(v) {
    val Binding = SugrenciascardBinding.bind(v)

    fun render(list: ropamodel, onAddCart: (ropamodel)->Unit){

        Picasso.get().load(list.Imagen1).into(Binding.imageView2)
        Binding.textView9.text = list.nombreRopa


        Binding.layoutSugerencias.setOnClickListener{
            onAddCart(list)
        }


    }
}