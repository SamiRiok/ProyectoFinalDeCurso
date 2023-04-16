package com.example.apppruebafinal.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.apppruebafinal.databinding.ActivityNoConnectionBinding

class NoConnection : AppCompatActivity() {
    lateinit var Binding: ActivityNoConnectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Binding = ActivityNoConnectionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(Binding.root)
        listener()
    }

    private fun listener() {
        Binding.button.setOnClickListener{
            finish()
        }
    }
}