package com.example.apppruebafinal.views


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.apppruebafinal.R
import com.example.apppruebafinal.databinding.ActivityHomeBinding
import com.example.apppruebafinal.fragments.*
import com.example.apppruebafinal.preferences.sharedPreferences
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity(), MethodsSesion {
    lateinit var Binding: ActivityHomeBinding
    private val inicioFragment = inicioFragment()
    private val carritoFragment = carritoFragment()
    private val perfilFragment = PerfilFragment()
    private val megustaFragment = MeGustasFragment()
    private val regaloFragment = RegaloFragment()


    lateinit var prefs: sharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)
        navigationControll()


        cargarFragments(inicioFragment)
        prefs = sharedPreferences(this)


    }

    private fun navigationControll() {
        val inicioNav: MenuItem = Binding.bottomNavigation.menu.findItem(R.id.inicioNav)
        inicioNav.isChecked = true

        Binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.CarritoNav -> cargarFragments(carritoFragment)
                R.id.inicioNav -> cargarFragments(inicioFragment)
                R.id.perfilNav -> cargarFragments(perfilFragment)
                R.id.MegustaNav -> cargarFragments(megustaFragment)
                R.id.RegaloNav -> cargarFragments(regaloFragment)
            }
            true
        }

    }



    private fun cargarFragments(fragment: Fragment?) {
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(Binding.inicioFragment.id, fragment)
            transaction.commit()
        }

    }

    override fun cierreSesion() {
        super.cierreSesion()
        prefs.borrarPrefs()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()

    }






}