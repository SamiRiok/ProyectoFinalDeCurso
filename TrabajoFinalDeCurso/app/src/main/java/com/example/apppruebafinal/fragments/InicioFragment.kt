package com.example.apppruebafinal.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apppruebafinal.views.InfoActivity
import com.example.apppruebafinal.R
import com.example.apppruebafinal.adapters.adapterRopa.ropaAdapter
import com.example.apppruebafinal.databinding.FragmentInicioBinding
import com.example.apppruebafinal.modelos.MegustaUpdate
import com.example.apppruebafinal.modelos.modeloRopa.ropamodel
import com.example.apppruebafinal.preferences.sharedPreferences
import kotlinx.coroutines.launch


class inicioFragment : Fragment(), MethodsSesion {
    private lateinit var Binding: FragmentInicioBinding
    var listener: MethodsSesion? = null
    lateinit var adapter: ropaAdapter
    var list = mutableListOf<ropamodel>()
    var page = 1
    val key = "4c89e9fb3a2a30bc1060a776c4590699"
    var tipo = ""
    var color = ""
    var talla = ""
    var sexo = ""
    var megusta = ""
    lateinit var prefs: sharedPreferences
    var scrolly = 287

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Binding = FragmentInicioBinding.inflate(inflater, container, false)
        return Binding.root



    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = sharedPreferences(Binding.Menu.context)
        Binding.Tallasp2.visibility = View.GONE

        recogerMegustas()
        setImages()
        setListener()
        traerRopa(true)
        pintarRopa()
    }

    private fun recogerMegustas() {

        lifecycleScope.launch{
            val datos = com.example.apppruebafinal.api.Client.apiClient.getUsers(key, prefs.cogerEmail().toString())
            megusta = datos.megusta.toString()
            prefs.guardarMegusta(megusta)

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MethodsSesion) listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    private fun pintarRopa() {
        val layoutManager = GridLayoutManager(Binding.Menu.context, 2)
        Binding.RopaRecyler.layoutManager = layoutManager

        adapter = ropaAdapter(list, {ropamodel -> onAddCart(ropamodel)},{ropamodel ->  info(ropamodel)}){ropamodel ->  megusta(ropamodel)}
        Binding.RopaRecyler.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun megusta(modelo: ropamodel) {
        if(megusta.isNullOrEmpty()){
            megusta += ","+modelo.id

        }
        else{
            if(!megusta.contains(modelo.id)){
                megusta += ","+modelo.id

            }
            else{
                if(megusta == "null,"+modelo.id){
                    megusta = "null"
                    prefs.guardarMegusta(megusta)
                    Binding.RopaRecyler.adapter?.notifyDataSetChanged()
                }
                else{
                    var megustalist = megusta.split(",")
                    var megusta2 = ""
                    var contador = 0
                    for ( i in megustalist){
                        if(i != modelo.id && i.isNotEmpty()){
                            if(contador == 0){
                                megusta2 += "$i"
                                contador += 1
                            }
                            else{
                                megusta2 += ",$i"
                            }
                        }
                    }
                    if(megusta2 == ",null"){
                        megusta2 = "null"
                    }
                    megusta = megusta2
                    Binding.RopaRecyler.adapter?.notifyDataSetChanged()
                }
            }
        }

        if (megusta != null) {
            lifecycleScope.launch{
                com.example.apppruebafinal.api.Client.apiClient.updateMegusta(MegustaUpdate(key, prefs.cogerID().toString(), "true", megusta))
            }
            prefs.guardarMegusta(megusta)
            Binding.RopaRecyler.adapter?.notifyDataSetChanged()
        }
    }

    private fun info(it: ropamodel) {
        startActivity(Intent(Binding.Tallasp2.context, InfoActivity::class.java).apply {
            putExtra("MODELO", it)
        })
    }

    private fun setImages() {
        Binding.menuTipos.visibility = View.GONE
        Binding.Menu.setBackgroundResource(android.R.drawable.ic_menu_sort_by_size)
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListener() {
        var poderbajar = true
        Binding.volverArriba.visibility = View.INVISIBLE
        Binding.rellenoSuperior.visibility = View.GONE

        if(prefs.cogerSugerencias2().isNullOrEmpty()){
            Binding.layoutSugerenciaCarrito2.visibility = View.VISIBLE
        }
        else{
            Binding.layoutSugerenciaCarrito2.visibility = View.GONE
        }

        var contadorPulsa = 0
        Binding.cerrarSugerencia2.setOnClickListener{

            if(contadorPulsa == 1){
                Binding.layoutSugerenciaCarrito2.visibility = View.GONE
                prefs.GuardarSugerencias2("ocultar")
            }

            else if(contadorPulsa == 0){
                Binding.textoSugerencia2.text = "Cuando te deslices por los diferentes articulos se ocultara la barra superior, puedes volver arriba pulsando la flecha."
                contadorPulsa += 1
            }

        }

        Binding.RopaRecyler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && poderbajar) {
                    Binding.RopaRecyler.isNestedScrollingEnabled = false
                    Binding.scroll.smoothScrollTo(0, Binding.scroll.maxScrollAmount)
                    poderbajar = false
                    Binding.RopaRecyler.isNestedScrollingEnabled = true
                    Binding.volverArriba.visibility = View.VISIBLE
                }


            }
        })

        Binding.scroll.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                if(Binding.scroll.scrollY == 0){
                    poderbajar = true
                    Binding.volverArriba.visibility = View.INVISIBLE
                }
                if(Binding.scroll.scrollY > 0){
                    Binding.volverArriba.visibility = View.VISIBLE

                }
                if(Binding.scroll.scrollY != Binding.scroll.maxScrollAmount){
                    poderbajar = true

                }
            }

        })

        Binding.volverArriba.setOnClickListener{
            Binding.scroll.smoothScrollTo(0, 0)
            poderbajar = true
            Binding.volverArriba.visibility = View.INVISIBLE

        }


        Binding.scroll.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }

        Binding.siguienteRopa.setOnClickListener{
            if(color != "" || talla != "" || tipo != "" || sexo != ""){
                page += 1
                Binding.scroll.post(Runnable {
                    Binding.scroll.scrollTo(0,0)
                })
                Binding.RopaRecyler.scrollTo(0,0)
                traerRopaporSexo(true)
            }
            else {
                page += 1
                Binding.scroll.post(Runnable {
                    Binding.scroll.scrollTo(0,0)
                })
                Binding.RopaRecyler.scrollTo(0,0)

                traerRopa(true)
                Binding.NumeroPaginaTX.text = "Pag.$page"
            }

        }
        Binding.atrasRopa.setOnClickListener{
            if(color != "" || talla != "" || tipo != "" || sexo != ""){
                page -= 1
                Binding.scroll.post(Runnable {
                    Binding.scroll.scrollTo(0,0)
                })
                Binding.RopaRecyler.scrollTo(0,0)
                traerRopaporSexo(true)
            }
            else {
                page -= 1
                Binding.scroll.post(Runnable {
                    Binding.scroll.scrollTo(0,0)
                })
                Binding.RopaRecyler.scrollTo(0,0)

                traerRopa(true)
                Binding.NumeroPaginaTX.text = "Pag.$page"
            }


        }
        Binding.NumeroPaginaTX.text = "Pag.$page"

        Binding.Menu.setOnClickListener {

            if (Binding.menuTipos.visibility == View.VISIBLE) {
                Binding.menuTipos.visibility = View.GONE
                Binding.scroll.scrollTo(0,0)


            } else {
                Binding.menuTipos.visibility = View.VISIBLE
                Binding.scroll.scrollTo(0,0)

            }

        }

        val typeface = resources.getFont(R.font.bebas)

        Binding.RopaHombre.setOnClickListener {

            sexo = "hombre"
            Binding.txtHombre.setTypeface(typeface, Typeface.BOLD)
            Binding.txtMujer.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtNiO.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtTodo.setTypeface(typeface, Typeface.NORMAL)
            Binding.Tallasp.setSelection(0)
            Binding.Colorsp.setSelection(0)
            Binding.TipoSp.setSelection(0)
            page = 1
            Binding.NumeroPaginaTX.text = "Pag.$page"
            traerRopaporSexo(true)


        }
        Binding.RopaMujer.setOnClickListener {

            sexo = "mujer"
            Binding.txtHombre.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtMujer.setTypeface(typeface, Typeface.BOLD)
            Binding.txtNiO.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtTodo.setTypeface(typeface, Typeface.NORMAL)
            Binding.Tallasp.setSelection(0)
            Binding.Colorsp.setSelection(0)
            Binding.TipoSp.setSelection(0)
            page = 1
            Binding.NumeroPaginaTX.text = "Pag.$page"
            traerRopaporSexo(true)

        }
        Binding.RopaNiO.setOnClickListener {
            sexo = "nino"
            Binding.txtHombre.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtMujer.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtNiO.setTypeface(typeface, Typeface.BOLD)
            Binding.txtTodo.setTypeface(typeface, Typeface.NORMAL)
            Binding.Tallasp2.setSelection(0)
            Binding.Tallasp.setSelection(0)
            Binding.Colorsp.setSelection(0)
            Binding.TipoSp.setSelection(0)
            page = 1
            Binding.NumeroPaginaTX.text = "Pag.$page"
            traerRopaporSexo(true)

        }
        Binding.MostrarTodo.setOnClickListener {
            sexo = ""
            Binding.txtHombre.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtMujer.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtNiO.setTypeface(typeface, Typeface.NORMAL)
            Binding.txtTodo.setTypeface(typeface, Typeface.BOLD)

            Binding.Tallasp.setSelection(0)
            Binding.Colorsp.setSelection(0)
            Binding.TipoSp.setSelection(0)
            page = 1
            Binding.NumeroPaginaTX.text = "Pag.$page"
            traerRopaporSexo(true)
        }




        Binding.TipoSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                if(Binding.TipoSp.selectedItem.toString() == "Mostrar Todo"){
                    Binding.textTipo.text = "Tipo:"
                    tipo = ""


                }
                else{
                    Binding.textTipo.text = Binding.TipoSp.selectedItem.toString()
                    tipo = Binding.TipoSp.selectedItem.toString()
                }


                page = 1
                Binding.NumeroPaginaTX.text = "Pag.$page"
                traerRopaporSexo(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        Binding.Colorsp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                if(Binding.Colorsp.selectedItem.toString() == "Mostrar Todo"){
                    Binding.textColor.text = "Color:"
                    color = ""
                }
                else{
                    Binding.textColor.text = Binding.Colorsp.selectedItem.toString().substring(2)
                    color = Binding.Colorsp.selectedItem.toString().substring(2)
                    if(color == "egro"){
                        color = "negro"
                        Binding.textColor.text = color
                    }
                }
                page = 1
                Binding.NumeroPaginaTX.text = "Pag.$page"
                traerRopaporSexo(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        Binding.Tallasp2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?,iew: View?,position: Int,id: Long) {
                if(Binding.Tallasp2.selectedItem.toString() == "Mostrar Todo"){
                    Binding.textTalla.text = "Talla:"
                    talla = ""


                }
                else{
                    Binding.textTalla.text = Binding.Tallasp2.selectedItem.toString()
                    talla = Binding.Tallasp2.selectedItem.toString()


                }
                page = 1
                Binding.NumeroPaginaTX.text = "Pag.$page"
                traerRopaporSexo(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        Binding.Tallasp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    if(Binding.Tallasp.selectedItem.toString() == "Mostrar Todo"){
                        Binding.textTalla.text = "Talla:"
                        talla = ""


                    }
                    else{
                        Binding.textTalla.text = Binding.Tallasp.selectedItem.toString()
                        talla = Binding.Tallasp.selectedItem.toString()


                    }
                page = 1
                Binding.NumeroPaginaTX.text = "Pag.$page"
                traerRopaporSexo(true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }



        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun traerRopaporSexo(bajar: Boolean) {
        if(bajar){
            Binding.RopaRecyler.layoutManager?.scrollToPosition(0)
            Binding.scroll.post(Runnable {
                Binding.scroll.scrollTo(0,0)
            })
        }

        if(sexo != "niño"){
            Binding.Tallasp2.visibility = View.GONE
            Binding.Tallasp.visibility = View.VISIBLE
        }
        else{
            Binding.Tallasp2.visibility = View.VISIBLE
            Binding.Tallasp.visibility = View.GONE
        }

        if(sexo == "" && talla == "" && tipo == "" && color == ""){
            traerRopa(true)
            return
        }
        lifecycleScope.launch{
            val ropa = com.example.apppruebafinal.api.apiRopa.Client.apiClientRopa.getClothesByType(key,page.toString(),tipo,color,talla,sexo)
            if(!ropa.hits.isNullOrEmpty()){
                adapter.list = ropa.hits.toMutableList()
                adapter.notifyDataSetChanged()
                Binding.Vacio.visibility = View.INVISIBLE

                if(ropa.results > page*20){
                    Binding.siguienteRopa.visibility = View.VISIBLE
                }
                else{
                    Binding.siguienteRopa.visibility = View.GONE
                }

                if(page == 1){
                    Binding.atrasRopa.visibility = View.GONE
                }
                else{
                    Binding.atrasRopa.visibility = View.VISIBLE
                }

                if(Binding.atrasRopa.visibility != View.GONE || Binding.siguienteRopa.visibility != View.GONE){
                    Binding.rellenoSuperior.visibility = View.INVISIBLE

                    val layoutParams = Binding.RopaRecyler.layoutParams
                    val scale = resources.displayMetrics.density
                    val pixels = (610  * scale + 0.5f).toInt()
                    layoutParams.height = pixels
                    Binding.RopaRecyler.layoutParams = layoutParams
                }
                if(Binding.siguienteRopa.visibility == View.GONE && Binding.atrasRopa.visibility == View.GONE){
                    Binding.rellenoSuperior.visibility = View.INVISIBLE

                    val layoutParams = Binding.RopaRecyler.layoutParams
                    val scale = resources.displayMetrics.density
                    val pixels = (650 * scale + 0.5f).toInt()
                    layoutParams.height = pixels
                    Binding.RopaRecyler.layoutParams = layoutParams
                }
                Binding.NumeroPaginaTX.text = "Pag."+ropa.pagina.toString()

            }
            else{
                adapter.list.clear()
                adapter.notifyDataSetChanged()
                Binding.Vacio.visibility = View.VISIBLE
                Binding.siguienteRopa.visibility = View.GONE
                Binding.atrasRopa.visibility = View.GONE
                Binding.NumeroPaginaTX.visibility = View.GONE

            }


        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun traerRopa(bajar: Boolean){
        if(bajar){
            Binding.RopaRecyler.layoutManager?.scrollToPosition(0)
            Binding.scroll.post(Runnable {
                Binding.scroll.scrollTo(0,0)
            })
        }

        lifecycleScope.launch{
            val ropa = com.example.apppruebafinal.api.apiRopa.Client.apiClientRopa.getClothes(key, page.toString())
            adapter.list = ropa.hits.toMutableList()

            adapter.notifyDataSetChanged()
            Binding.Vacio.visibility = View.INVISIBLE
            if(ropa.results >  page*20){
                Binding.siguienteRopa.visibility = View.VISIBLE
                Binding.NumeroPaginaTX.visibility = View.VISIBLE


            }
            else{
                Binding.siguienteRopa.visibility = View.GONE
            }

            if(page == 1){
                Binding.atrasRopa.visibility = View.GONE

            }
            else{
                Binding.atrasRopa.visibility = View.VISIBLE

            }
            if(Binding.atrasRopa.visibility != View.GONE || Binding.siguienteRopa.visibility != View.GONE){
                Binding.rellenoSuperior.visibility = View.INVISIBLE

                val layoutParams = Binding.RopaRecyler.layoutParams
                val scale = resources.displayMetrics.density
                val pixels = (610  * scale + 0.5f).toInt()
                layoutParams.height = pixels
                Binding.RopaRecyler.layoutParams = layoutParams
            }
            if(Binding.siguienteRopa.visibility == View.GONE && Binding.atrasRopa.visibility == View.GONE){
                Binding.rellenoSuperior.visibility = View.INVISIBLE

                val layoutParams = Binding.RopaRecyler.layoutParams
                val scale = resources.displayMetrics.density
                val pixels = (650 * scale + 0.5f).toInt()
                layoutParams.height = pixels
                Binding.RopaRecyler.layoutParams = layoutParams
            }
            Binding.NumeroPaginaTX.text = "Pag."+ropa.pagina.toString()

        }
    }
    @SuppressLint("NotifyDataSetChanged")

    private fun onAddCart(ropa: ropamodel) {
        var carrito = prefs.cogerCarrito()
        if(carrito.isNullOrEmpty()){
            carrito += ropa.id

        }
        else{
            if(!carrito.contains(ropa.id)){
                carrito += ","+ropa.id

            }

        }
        if (carrito != null) {
            prefs.guardarCarrito(carrito)
            Binding.RopaRecyler.adapter?.notifyDataSetChanged()
        }


    }

    override fun onResume() {
        Binding.RopaRecyler.adapter?.notifyDataSetChanged()
        super.onResume()
    }





}