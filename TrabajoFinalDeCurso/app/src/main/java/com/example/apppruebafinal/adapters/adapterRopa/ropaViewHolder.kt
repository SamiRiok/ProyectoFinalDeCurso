package com.example.apppruebafinal.adapters.adapterRopa

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apppruebafinal.R
import com.example.apppruebafinal.api.Client
import com.example.apppruebafinal.databinding.RopacardBinding
import com.example.apppruebafinal.fragments.MethodsSesion
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.example.apppruebafinal.preferences.sharedPreferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class ropaViewHolder(v: View): ViewHolder(v), MethodsSesion {
    val Binding = RopacardBinding.bind(v)
    val prefs = sharedPreferences(Binding.iamgen.context)

    @SuppressLint("SetTextI18n")
    fun render(list: ropamodel, onAddCart: (ropamodel)->Unit, info: (ropamodel)-> Unit, megusta: (ropamodel)-> Unit){

        //RECOGER MEGUSTA



        //--------

        Picasso.get().load(list.Imagen1).into(Binding.iamgen)
        Binding.nombre.text = list.nombreRopa
        Binding.SexoCard.text = list.Sexo
        Binding.precio.text = "Precio: "+list.Precio+" €"
        Binding.talla.text = "Talla: "+list.Talla

        if(!prefs.cogerCarrito().isNullOrEmpty()){
            if(prefs.cogerCarrito()!!.contains(list.id)){
                Binding.aAdir.visibility = View.INVISIBLE
                Binding.aAdido.visibility = View.VISIBLE
            }
            else{
                Binding.aAdir.visibility = View.VISIBLE
                Binding.aAdido.visibility = View.INVISIBLE

            }

        }

        Binding.aAdir.setOnClickListener{
            onAddCart(list)
        }
        if(prefs.cogerMegusta()!!.contains(list.id)){
            Binding.Corazon.setImageResource(R.drawable.corazonrelleno)
        }
        else{
            Binding.Corazon.setImageResource(R.drawable.corazonvacio)

        }

        Binding.Corazon.setOnClickListener{
            megusta(list)
        }

        Binding.cardROpa.setOnClickListener{
            info(list)
        }

    }

}