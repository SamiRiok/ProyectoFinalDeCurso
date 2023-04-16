package com.example.apppruebafinal.adapters.adapterCarrito

import android.annotation.SuppressLint
import android.content.Context
import android.renderscript.ScriptGroup.Binding
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.apppruebafinal.databinding.CarritocardBinding
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebafinal.R
import com.example.apppruebafinal.preferences.sharedPreferences


class carritoViewHolder(v: View): ViewHolder(v){
    val Binding = CarritocardBinding.bind(v)
    private lateinit var gestureDetector: GestureDetectorCompat
    var mostrarCantidad = false
    var indexSelected = 0
    var prefs = sharedPreferences(Binding.cardViewCarrito.context)

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    fun render(list: ropamodel, info: (ropamodel)->Unit, cambioCantidad: (Int, Int, String,String)->Unit){

        var cantidadEx = prefs.cogerCantidades()



        Picasso.get().load(list.Imagen1).into(Binding.imagenCarrito)
        Binding.nombreRopaCarrito.text = list.nombreRopa
        Binding.precioRopaCarrito.text = list.Precio + " €"

        if(!mostrarCantidad){
            Binding.CantidadCarrito.visibility = View.GONE

        }

        var elements = mutableListOf<Int>()
        for (i in 1..list.Cantidad.toInt()){
            elements.add(i)
        }
        val adapter = ArrayAdapter(Binding.cardViewCarrito.context, android.R.layout.simple_spinner_item, elements)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Binding.SpCantidad.adapter = adapter
        Binding.imagenCarrito.setOnClickListener {
            info(list)
        }

        gestureDetector = GestureDetectorCompat(Binding.cardViewCarrito.context, MyGestureListener())

        Binding.cardViewCarrito.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)

            true
        }
        Binding.SpCantidad.setSelection(indexSelected)
        Binding.txCantidadActual.text = "${list.Precio}€ x1."

        if(!cantidadEx.isNullOrEmpty()){
            if (cantidadEx.contains(list.id)){
                if(cantidadEx.contains(",")){
                    var cogerCantidad = cantidadEx.split(",")
                    for (i in cogerCantidad){
                        if (i.contains(list.id)){
                            var guion = i.split("-")
                            Binding.SpCantidad.setSelection(guion[1].toInt()-1)
                            Binding.txCantidadActual.text = "${list.Precio}€ x${guion[1]}."
                            Binding.precioRopaCarrito.text = (list.Precio.toInt() * Binding.SpCantidad.selectedItem.toString().toInt()).toString() + " €"                        }
                    }
                }
                else{
                    var guion = cantidadEx.split("-")
                    Binding.SpCantidad.setSelection(guion[1].toInt()-1)
                    Binding.txCantidadActual.text = "${list.Precio}€ x${guion[1]}."
                    Binding.precioRopaCarrito.text = (list.Precio.toInt() * Binding.SpCantidad.selectedItem.toString().toInt()).toString() + " €"
                }

            }
        }

        Binding.SpCantidad.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var euros = Binding.precioRopaCarrito.text.toString().replace(" €","")
                cambioCantidad(list.Precio.toInt() *  Binding.SpCantidad.selectedItem.toString().toInt(), euros.toInt(), list.id, Binding.SpCantidad.selectedItem.toString())
                Binding.precioRopaCarrito.text = (list.Precio.toInt() *  Binding.SpCantidad.selectedItem.toString().toInt()).toString()+ " €"
                indexSelected = p2
                Binding.txCantidadActual.text = "${list.Precio}€ x${Binding.SpCantidad.selectedItem}."

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }



    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(event: MotionEvent): Boolean {
            return true
        }

        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffX = event2.x - event1.x
            val diffY = event2.y - event1.y
            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    if(Binding.CantidadCarrito.visibility == View.GONE){
                        Binding.CantidadCarrito.visibility = View.VISIBLE
                        mostrarCantidad = true

                    }
                    else{
                        Binding.CantidadCarrito.visibility = View.GONE
                        mostrarCantidad = false
                    }
                    return true
                }

            }
            return false
        }
    }

}




