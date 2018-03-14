package com.example.carlos.projetocultural.utils

import android.app.DialogFragment
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.ListviewpubpesqActivity
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.db.*

//perifica a internet
object AndroidUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
                .map { connectivity.getNetworkInfo(it) }
                .any { it.state == NetworkInfo.State.CONNECTED };
    }




    fun savePubpesq(ca: ToDoAdapter?, databaseOpenHelper: MyDatabaseOpenHelper?, nome:String, redesocial:String, endereco:String, contato:String, email:String, atvexercida:String, categoria:String, campo1:String, campo2:String,
                    campo3:String, campo4:String, campo5:String, longitude:String, latitude:String, pesquisador:String,anoinicio:String,cnpj:String,
                    representacao:String,recurso:String,base64_1:String,base64_2:String,base64_3:String,base64_4:String
    ){
        databaseOpenHelper?.use{
            insert("publicacaop",
                    "nome" to nome,
                    "redesocial" to redesocial,
                    "endereco" to endereco,
                    "contato" to contato,
                    "email" to email,
                    "atvexercida" to atvexercida,
                    "categoria" to categoria,
                    "anoinicio" to anoinicio,
                    "cnpj" to cnpj,
                    "representacao" to representacao,
                    "recurso" to recurso,
                    "longitude" to longitude,
                    "latitude" to latitude,
                    "pesquisador" to pesquisador,
                    "img1" to base64_1,
                    "img2" to base64_2,
                    "img3" to base64_3,
                    "img4" to base64_4,
                    "campo1" to campo1,
                    "campo2" to campo2,
                    "campo3" to campo3,
                    "campo4" to campo4,
                    "campo5" to campo5
            )
        }
    }


    fun updateTablepesq(ca: ToDoAdapter?,databaseOpenHelper: MyDatabaseOpenHelper?, nome:String, redesocial:String, endereco:String, contato:String, email:String, atvexercida:String, categoria:String, campo1:String, campo2:String,
                        campo3:String, campo4:String, campo5:String, longitude:String, latitude:String, pesquisador:String,anoinicio:String,cnpj:String,
                        representacao:String,recurso:String,base64_1:String?,base64_2:String?,base64_3:String?,base64_4:String?,idx:Int
    ){


        databaseOpenHelper?.use {
            update("publicacaop",
                    "nome" to nome,
                    "redesocial" to redesocial,
                    "endereco" to endereco,
                    "contato" to contato,
                    "email" to email,
                    "atvexercida" to atvexercida,
                    "categoria" to categoria,
                    "anoinicio" to anoinicio,
                    "cnpj" to cnpj,
                    "representacao" to representacao,
                    "recurso" to recurso,
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "pesquisador" to pesquisador,
                    "img1" to base64_1,
                    "img2" to base64_2,
                    "img3" to base64_3,
                    "img4" to base64_4,
                    "campo1" to campo1,
                    "campo2" to campo2,
                    "campo3" to campo3,
                    "campo4" to campo4,
                    "campo5" to campo5
            )
                    .where("_id = \""+idx+"\"")
                    .exec()
        }
        val todoCursor= databaseOpenHelper?.writableDatabase?.rawQuery("Select * from publicacaop",null)
        ca?.swapCursor(todoCursor)
        //fragment.dismiss() //fecha o fragment
    }




}