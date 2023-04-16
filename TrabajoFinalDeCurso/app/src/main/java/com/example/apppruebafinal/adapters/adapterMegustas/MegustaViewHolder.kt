package com.example.apppruebafinal.adapters.adapterMegustas

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.squareup.picasso.Picasso
import com.example.apppruebafinal.databinding.SugrenciascardBinding


class MegustaViewHolder(v: View): ViewHolder(v){
    val Binding = SugrenciascardBinding.bind(v)

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    fun render(list: ropamodel, info: (ropamodel)->Unit){


        Picasso.get().load(list.Imagen1).into(Binding.imageView2)
        Binding.textView9.text = list.nombreRopa

        Binding.layoutSugerencias.setOnClickListener {
            info(list)
        }

    }



}


