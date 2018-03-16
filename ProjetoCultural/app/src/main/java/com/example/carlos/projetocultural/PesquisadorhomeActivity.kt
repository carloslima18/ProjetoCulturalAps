package com.example.carlos.projetocultural


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pesquisadorhome.*
import org.jetbrains.anko.browse

class PesquisadorhomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesquisadorhome)
    }

    override fun onResume() {
        super.onResume()
        buttonviewpubpesq.setOnClickListener {
            startActivity(Intent(this, ListviewpubpesqActivity::class.java))
        }
        buttonaddpubpesq.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }
        buttonviewmappubpesq.setOnClickListener {
            browse("http://192.168.15.5:8765/")
        }
    }
}
