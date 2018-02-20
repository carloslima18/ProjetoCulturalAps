package com.example.carlos.projetocultural

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_front.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        vermais.visibility = View.VISIBLE
        texto2.visibility = View.INVISIBLE

    }

    override fun onResume() {
        super.onResume()
        buttonComentario.setOnClickListener {
            browse("http://www.anapolis.go.gov.br/portal/multimidia/noticias/ver/inscriasames-para-o-programa-estapo-abertas")
        }

        vermais.setOnClickListener{
            texto2.visibility = View.VISIBLE
            texto2.text = resources.getString(R.string.noticias2)
            vermais.visibility = View.INVISIBLE
        }

    }
}
