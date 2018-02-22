package com.example.carlos.projetocultural

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.carlos.projetocultural.extensions.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btLogin = findViewById<Button>(R.id.btnSenha)
        btnSenha.setOnClickListener{
            val tsenha = findViewById<EditText>(R.id.tSenha)
            val tlogin = findViewById<EditText>(R.id.tLogin)
            if("sandro" == tlogin.text.toString() && tsenha.text.toString() == "123"){
                toast("Bem vindo")
            }else{
                toast("senha ou Login incorretos")
            }
        }
    }

}
