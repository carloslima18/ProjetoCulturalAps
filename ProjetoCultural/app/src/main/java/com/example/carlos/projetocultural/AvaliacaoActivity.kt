package com.example.carlos.projetocultural

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*

import kotlinx.android.synthetic.main.avaliacao.*
import android.widget.RatingBar.OnRatingBarChangeListener
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.toast
import kotlinx.android.synthetic.main.content_avaliacao.*
import kotlinx.android.synthetic.main.list_row_main.*
import org.json.JSONObject

class AvaliacaoActivity : AppCompatActivity() {

    private var ratingBar: RatingBar? = null
    private var txtValorAvaliacao: TextView? = null
    private var btnSubmit: Button? = null


    var id :String ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avaliacao)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        id = extras.getString("idpub")

    }

    override fun onResume() {
        super.onResume()
        val bt = findViewById<Button>(R.id.btnSubmit) as Button
        val opinao = findViewById<EditText>(R.id.opiniao) as EditText
        val rating = findViewById<RatingBar>(R.id.ratingBar) as RatingBar
        bt.setOnClickListener {
            val op = opinao.text
            if(op == null){
                toast("dê sua opinião")
            }else {
                val valor = rating.getRating()
                val data = JSONObject();
                data.put("idpub", id)
                data.put("nota", valor)
                data.put("opiniao", op)
                /* Thread{
                pubService.saveAvaliacaopub(data)
                runOnUiThread{
                    toast("avaliação enviada. Obrigado.")
                }
            } */
                toast("avaliação enviada. Obrigado" + valor.toString())
            }

        }
    }



}
