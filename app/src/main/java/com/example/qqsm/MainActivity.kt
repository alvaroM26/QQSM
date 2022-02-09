package com.example.qqsm

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.qqsm.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var contPreguntas = 0
    var contAciertos = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cogerPregunta()

    }

    fun cogerPregunta() {

        binding.enviarRespuesta.visibility = View.GONE
        binding.progressBar.visibility= View.VISIBLE

        var respuestaSeleccionada = ""

        val client = OkHttpClient()

        val request = Request.Builder()
        request.url("http://10.0.2.2:8084/Pregunta")
        val call = client.newCall(request.build())

        call.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                CoroutineScope(Dispatchers.Main).launch {

                    Toast.makeText(this@MainActivity, "Algo ha ido mal", Toast.LENGTH_SHORT).show()

                }

            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {

                println(response.toString())

                response.body?.let { responseBody ->

                    val body = responseBody.string()
                    println(body)
                    val gson = Gson()

                    val pregunta = gson.fromJson(body,Preguntas::class.java) //PASAR LA CLASE PREGUNTAS A LA VARIABLE PREGUNTA PARA UTILIZARLA

                    println(pregunta)

                    CoroutineScope(Dispatchers.Main).launch {

                        binding.progressBar.visibility = View.GONE
                        binding.preguntaAResolver.text =" " + pregunta.id + " - " + pregunta.pregunta
                        binding.bt1.text = pregunta.respuesta1
                        binding.bt2.text = pregunta.respuesta2
                        binding.bt3.text = pregunta.respuesta3
                        binding.bt4.text = pregunta.respuesta4

                        binding.bt1.setOnClickListener {

                            binding.enviarRespuesta.visibility = View.VISIBLE
                            respuestaSeleccionada = binding.bt1.text.toString()
                            cambiarEstilo(binding.bt1)

                        }

                        binding.bt2.setOnClickListener {

                            binding.enviarRespuesta.visibility = View.VISIBLE
                            respuestaSeleccionada = binding.bt2.text.toString()
                            cambiarEstilo(binding.bt2)

                        }

                        binding.bt3.setOnClickListener {

                            binding.enviarRespuesta.visibility = View.VISIBLE
                            respuestaSeleccionada = binding.bt3.text.toString()
                            cambiarEstilo(binding.bt3)

                        }

                        binding.bt4.setOnClickListener {

                            binding.enviarRespuesta.visibility = View.VISIBLE
                            respuestaSeleccionada = binding.bt4.text.toString()
                            cambiarEstilo(binding.bt4)

                        }

                        binding.enviarRespuesta.setOnClickListener {
                            enviarInformacion(pregunta.id, respuestaSeleccionada)

                        }

                    }

                }

            }

        })

    }

    fun cambiarEstilo(button: Button){
        button.setBackgroundColor(Color.YELLOW)
        button.setTextColor(Color.BLACK)
    }

    fun enviarInformacion(id: Int, respuesta: String) {

        binding.progressBar.visibility = View.VISIBLE

        val clientButton = OkHttpClient()

        val requestButton = Request.Builder()
        requestButton.url("http://10.0.2.2:8084/Pregunta/$id/$respuesta")

        val callButton = clientButton.newCall(requestButton.build())

        callButton.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                CoroutineScope(Dispatchers.Main).launch {

                    Toast.makeText(this@MainActivity, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE

                }

            }

            override fun onResponse(call: Call, response: Response) {

                println(response.toString())

                response.body?.let { responseBody ->

                    val body = responseBody.string()
                    println(body)

                    CoroutineScope(Dispatchers.Main).launch {

                        val respuestaCorrecta = "Respuesta Correcta"
                        binding.progressBar.visibility = View.GONE

                        Snackbar.make(binding.root,body,Snackbar.LENGTH_LONG).show()
                        if (body.contains(respuestaCorrecta)) {

                            contAciertos++
                            contPreguntas++

                        } else

                            contPreguntas++

                        binding.contadorTotales.text = contPreguntas.toString()
                        binding.contadorAcertadas.text = contAciertos.toString()

                        delay(2000)
                        binding.progressBar.visibility = View.VISIBLE

                        cogerPregunta()

                    }

                }

            }

        })

    }
}