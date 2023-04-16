package com.example.apppruebafinal.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apppruebafinal.databinding.FragmentPerfilBinding
import com.example.apppruebafinal.views.perfil.EditarPerfilView
import com.example.apppruebafinal.views.perfil.pedidosView


class PerfilFragment : Fragment() {
    lateinit var Binding : FragmentPerfilBinding
    var listener: MethodsSesion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        Binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()

    }

    private fun listener() {
        Binding.CerrarSesion.setOnClickListener{
            listener!!.cierreSesion()
        }
        Binding.MisPedidos.setOnClickListener{
            startActivity(Intent(Binding.MisPedidos.context, pedidosView::class.java))
        }
        Binding.EditarPerfil.setOnClickListener{
            startActivity(Intent(Binding.MisPedidos.context, EditarPerfilView::class.java))
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

}