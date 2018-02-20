package com.example.carlos.projetocultural

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import kotlinx.android.synthetic.main.activity_front.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email

class FrontActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);

        vermais.visibility = View.VISIBLE
        texto2.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        buttonComentario.setOnClickListener {
            val assunto = "Contato pelo projeto cultural mobile"
            email("carloslima519@outlook.com", assunto)
        }

        vermais.setOnClickListener{
            texto2.visibility = View.VISIBLE
            texto2.text = resources.getString(R.string.maintext2)
            vermais.visibility = View.INVISIBLE
        }

    }



}

