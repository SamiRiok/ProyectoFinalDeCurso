package com.example.apppruebafinal.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebafinal.views.InfoActivity
import com.example.apppruebafinal.adapters.adapterCarrito.carritoAdapter
import com.example.apppruebafinal.api.apiRopa.Client
import com.example.apppruebafinal.databinding.FragmentCarritoBinding
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.example.apppruebafinal.preferences.sharedPreferences
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


class carritoFragment : Fragment() {
    lateinit var Binding: FragmentCarritoBinding
    lateinit var prefs: sharedPreferences
    var carrito = ""
    val token = "4c89e9fb3a2a30bc1060a776c4590699"
    var list = mutableListOf<ropamodel>()
    lateinit var adapter: carritoAdapter
    var precioTotal = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return Binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = sharedPreferences(Binding.textView10.context)
        carrito = prefs.cogerCarrito().toString()
        listarRopa()
        listener()

        val touchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT){
                    delete(adapter.getItem(viewHolder.adapterPosition))
                    adapter.notifyDataSetChanged()

                }

            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val paint = Paint()
                    val icon: Bitmap

                    if (dX < 0) {
                        icon = BitmapFactory.decodeResource(resources, android.R.drawable.ic_menu_delete)
                        paint.color = Color.parseColor("#D60000")
                        val iconMargin = (itemView.height - icon.height) / 2
                        val iconTop = itemView.top + (itemView.height - icon.height) / 2
                        val iconBottom = iconTop + icon.height

                        val padding = 10

                        val left = itemView.right + dX + padding
                        val top = itemView.top.toFloat() + padding
                        val right = itemView.right.toFloat() - padding
                        val bottom = itemView.bottom.toFloat() - padding

                        val background = RectF(left, top, right, bottom)
                        c.drawRect(background, paint)

                        val iconLeft = itemView.right - iconMargin - icon.width
                        val iconRight = itemView.right - iconMargin
                        val iconRect = RectF(iconLeft.toFloat(), iconTop.toFloat(), iconRight.toFloat(), iconBottom.toFloat())
                        c.drawBitmap(icon, null, iconRect, paint)
                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(Binding.carritoRecyler)

    }


    private fun listener() {
        Binding.vaciarCarrito.setOnClickListener {
            prefs.borrarCarrito()
            carrito = prefs.cogerCarrito().toString()
            list.clear()
            listarRopa()
            traerRopa()
        }
        if(prefs.cogerSugerencias().isNullOrEmpty()){
            Binding.layoutSugerenciaCarrito.visibility = View.VISIBLE
        }
        else{
            Binding.layoutSugerenciaCarrito.visibility = View.GONE
        }
        var contadorPulsa = 0
        Binding.cerrarSugerencia.setOnClickListener{

            if(contadorPulsa == 2){
                Binding.layoutSugerenciaCarrito.visibility = View.GONE
                prefs.GuardarSugerencias("ocultar")
            }
            else if(contadorPulsa == 0){
                Binding.textoSugerencia.text = "Para ver los detalles de tu articulo pulsa la imagen del articulo que deseas visualizar"
                contadorPulsa += 1
            }
            else if(contadorPulsa == 1){
                Binding.textoSugerencia.text = "Para eliminar un articulo desplazalo hacia la izquierda hasta que mas de la mitad del articulo este fuera de pantalla."
                contadorPulsa += 1
            }

        }
    }


    private fun listarRopa() {
        Binding.vacioCarrito.visibility = View.VISIBLE
        Binding.vaciarCarrito.visibility = View.GONE
        Binding.PrecioTotal.visibility = View.GONE
        Binding.textView13.visibility = View.GONE
        Binding.ProcederCompra.visibility = View.GONE
        Binding.textView17.visibility = View.GONE
        Binding.codigoPromocion.visibility = View.GONE
        Binding.AplicarCodigo.visibility = View.GONE
        Binding.Envio.visibility = View.GONE
        Binding.txGastoDeEnvio.visibility = View.GONE


        if(!carrito.isNullOrEmpty()){
            Binding.vacioCarrito.text = "Cargando..."
            var lista = carrito.split(",")
            var cantidadLista = lista.size
            var contadorRopa = 0
            var cantidades = prefs.cogerCantidades()
            var contador = 0
            for (i in lista){
                lifecycleScope.launch {
                    val modelo = Client.apiClientRopa.getClothesId(token, i)
                    list.add(modelo)
                    if(!cantidades.isNullOrEmpty()){
                        if(cantidades.contains("${modelo.id}")){
                            if(cantidades.contains(",")){
                                var separados = cantidades.split(",")
                                for (i in separados){
                                    if(i.contains("${modelo.id}")){
                                        var guion = i.split("-")
                                        precioTotal += modelo.Precio.toInt() * guion[1].toInt()
                                    }
                                }
                            }
                            else{
                                var guion = cantidades.split("-")
                                precioTotal += modelo.Precio.toInt() * guion[1].toInt()
                            }
                        }
                        else{
                            precioTotal += modelo.Precio.toInt()
                        }
                    }
                    else{
                        precioTotal += modelo.Precio.toInt()
                    }

                    if(precioTotal < 100){
                        Binding.Envio.text = "Te faltan ${100-precioTotal} € para disfrutar del envio gratis"
                        Binding.Envio.setTextColor(Color.parseColor("#FF2700"))
                        Binding.txGastoDeEnvio.text = "+Gasto de envio: 3.99€"
                        Binding.txGastoDeEnvio.visibility = View.VISIBLE
                    }
                    else{
                        Binding.Envio.text = "Envio gratis, disfrutalo"
                        Binding.Envio.setTextColor(Color.parseColor("#00FF0C"))
                        Binding.txGastoDeEnvio.visibility = View.GONE

                    }
                    Binding.PrecioTotal.text = precioTotal.toString() + " €"
                    contadorRopa += 1
                    if(cantidadLista == contadorRopa){
                        traerRopa()
                        Binding.vacioCarrito.visibility = View.INVISIBLE
                        Binding.vaciarCarrito.visibility = View.VISIBLE
                        Binding.PrecioTotal.visibility = View.VISIBLE
                        Binding.textView13.visibility = View.VISIBLE
                        Binding.ProcederCompra.visibility = View.VISIBLE
                        Binding.textView17.visibility = View.VISIBLE
                        Binding.codigoPromocion.visibility = View.VISIBLE
                        Binding.AplicarCodigo.visibility = View.VISIBLE
                        Binding.Envio.visibility = View.VISIBLE
                    }
                }
            }
        }
        else{
            Binding.vacioCarrito.text = "TU CARRITO ESTA VACIO, por que no le das una oportunidad a algun articulo que te parezca interesante."
        }





    }



    private fun traerRopa() {
        val layoutmanager = LinearLayoutManager(Binding.textView10.context)
        Binding.carritoRecyler.layoutManager = layoutmanager
        adapter = carritoAdapter(list, {ropamodel->info(ropamodel)})
        {CambioPrecio,precio,ide, cantidad->cambioPrecio(CambioPrecio,precio,ide, cantidad)}
        Binding.carritoRecyler.adapter = adapter


    }

    @SuppressLint("SetTextI18n")
    private fun cambioPrecio(cambioPrecio: Int, PrecioAntiguo: Int, id:String, cantidad:String) {
        precioTotal -= PrecioAntiguo
        precioTotal += cambioPrecio
        if(precioTotal < 100){
            Binding.Envio.text = "Te faltan ${100-precioTotal} € para disfrutar del envio gratis"
            Binding.txGastoDeEnvio.text = "+Gasto de envio: 3.99€"
            Binding.txGastoDeEnvio.visibility = View.VISIBLE
            Binding.Envio.setTextColor(Color.parseColor("#FF2700"))
        }
        else{
            Binding.Envio.text = "Envio gratis, disfrutalo"
            Binding.txGastoDeEnvio.visibility = View.GONE
            Binding.Envio.setTextColor(Color.parseColor("#00FF0C"))
        }
        Binding.PrecioTotal.text = precioTotal.toString() + " €"

        var CantidadGuardar = "$id-$cantidad"
        if(prefs.cogerCantidades().isNullOrEmpty()){
            prefs.GuardarCantidades(CantidadGuardar)
        }
        else{
            var nuevasCantidades = ""
            if(prefs.cogerCantidades()!!.contains("$id")){
                if(!prefs.cogerCantidades()!!.contains(",")){
                    prefs.GuardarCantidades("$id-$cantidad")
                }
                else{
                    var cantidadesSep = prefs.cogerCantidades()!!.split(",")

                    for (i in cantidadesSep){
                        if (!i.contains("$id")){
                            if(nuevasCantidades.isNullOrEmpty()){
                                nuevasCantidades += i
                            }
                            else{
                                nuevasCantidades += ",$i"
                            }
                        }
                        else{
                            if(nuevasCantidades.isNullOrEmpty()){
                                nuevasCantidades += "$id-$cantidad"
                            }
                            else{
                                nuevasCantidades += ",$id-$cantidad"
                            }

                        }
                    }
                    prefs.GuardarCantidades(nuevasCantidades)
                }

            }
            else{
                if(prefs.cogerCantidades().isNullOrEmpty()){
                    prefs.GuardarCantidades("$id-$cantidad")
                }
                else{
                    prefs.GuardarCantidades(prefs.cogerCantidades() + ",$id-$cantidad")

                }

            }

        }

    }


    private fun delete(model: ropamodel) {
        var carritoo = prefs.cogerCarrito()
        var carrito2 = ""
        var carritoList = carritoo!!.split(",")



        for ( i in carritoList){
            if(i != model.id){
                if(carrito2.isNullOrEmpty()){
                    carrito2 += ""+i

                }
                else{
                    carrito2 += ",$i"
                }
            }
        }
        prefs.guardarCarrito(carrito2)
        carrito = prefs.cogerCarrito().toString()
        precioTotal = 0
        list.clear()
        listarRopa()
        Binding.carritoRecyler.adapter?.notifyDataSetChanged()

        var cantidades = prefs.cogerCantidades()
        var id = model.id

        if(cantidades!!.contains("$id")){
            var nuevasCantidades = ""
            if(!cantidades!!.contains(",")){
                prefs.GuardarCantidades("")
            }
            else{
                var separados = cantidades!!.split(",")
                for (i in separados){
                    if(!i.contains("$id")){
                        if(nuevasCantidades.isNullOrEmpty()){
                            nuevasCantidades += "$i"
                        }
                        else{
                            nuevasCantidades += ",$i"
                        }
                    }

                }
                prefs.GuardarCantidades(nuevasCantidades)
            }
        }


    }

    private fun info(model: ropamodel){
        startActivity(Intent(Binding.carritoRecyler.context, InfoActivity::class.java).apply {
            putExtra("MODELO", model)
        })
    }


    override fun onDetach() {
        super.onDetach()
        list.clear()
        precioTotal = 0
    }


}