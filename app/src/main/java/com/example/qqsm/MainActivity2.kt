package com.example.qqsm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.qqsm.databinding.ActivityLoginBinding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createAccount()

    }


    private fun createAccount(){

       var usuario = binding.campoUsuario.text.toString()
       var contraseña = binding.campoContraseA.text.toString()

       if (usuario.isEmpty() && contraseña.isEmpty()){
           binding.botonAceptar.visibility=View.GONE
       }else{
           binding.botonAceptar.visibility=View.VISIBLE
       }

        binding.botonAceptar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        }

    }