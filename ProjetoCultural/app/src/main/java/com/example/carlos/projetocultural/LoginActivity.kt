package com.example.carlos.projetocultural

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.extensions.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btLogin = findViewById<Button>(R.id.btnSenha)
        btnSenha.setOnClickListener{
            val tsenha = findViewById<EditText>(R.id.tSenha)
            val tlogin = findViewById<EditText>(R.id.tLogin)

            val intent = Intent(applicationContext, PesquisadorhomeActivity::class.java)
            toast("bem sucessido")
           // dialog.dismiss()
            startActivity(intent)

         /*   val dialog = ProgressDialog.show(this, "Um momento","Verificando",false,true)
            Thread{
                //&PublicacaoSearch[categoria]=feiras&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1"
                var listItems :ArrayList<JSONObject> = arrayListOf()
                val URL = "http://192.168.15.5/cult/sendpesquisador?_format=json&PesquisadorSearch[nome]=${tlogin.text}&PesquisadorSearch[senha]=${tsenha.text}&fields=campo1"
                listItems = pubService.getPub(URL)
                runOnUiThread {
                    val t = listItems[0].getString("campo1")
                    if(t == "ativo"){
                        val intent = Intent(applicationContext, formActivity::class.java)
                        toast("bem sucessido")
                        dialog.dismiss()
                        startActivity(intent)
                    }else{
                        toast("dados incorretos")
                    }
                }
            }.start() */
        }
    }

}
