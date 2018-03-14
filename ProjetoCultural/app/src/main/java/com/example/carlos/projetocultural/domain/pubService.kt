package com.example.carlos.projetocultural.domain

import android.content.Context
import android.util.Base64
import android.widget.Toast
import com.example.carlos.projetocultural.extensions.fromJson
import com.example.carlos.projetocultural.extensions.toJson
import com.example.carlos.projetocultural.utils.HttpHelper
import org.jetbrains.anko.toast
import org.json.JSONArray
import java.io.File
import org.json.JSONObject



object pubService{
    private val BASE_URL = "http://192.168.15.5/cult/sendpubuser?_format=json"
    fun getPub(cond:String):ArrayList<JSONObject>{
        try {
            val url = cond
            val json = HttpHelper.get(url)
            val arrayList = parserJson(json)
            return arrayList
        }catch (e:Exception){
            val a:ArrayList<JSONObject> = arrayListOf()
            return a
        }
    }

    fun getPub2(cond:String):ArrayList<JSONObject>{
        try {
            val url = cond
            val json = HttpHelper.get(url)
            val json2 = "[" + json + "]"
            val arrayList = parserJson(json2)
            return arrayList
        }catch (e:Exception){
            val a:ArrayList<JSONObject> = arrayListOf()
            return a
        }
    }

    fun getMap(cond: String): List<String> {
        val url = cond
        val json = HttpHelper.get(url)
        val list = fromJson<List<String>>(json)
        return list
    }


    // Salva uma publicação
    fun save(pub: JSONObject): Response {
        // Faz POST do JSON da pub
        val json = HttpHelper.post(BASE_URL, pub.toString())
        val response = fromJson<Response>(json)
        return response
    }

    fun saveAvaliacaopub(avl: JSONObject): Response{
        val json = HttpHelper.post("http://192.168.15.5/cult/sendpubuser?_format=json", avl.toString())
        val response = fromJson<Response>(json)
        return response
    }


    fun postFoto(file: File?): String {
        // Converte para Base64
        val bytes = file!!.readBytes()
        val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return base64
    }


    //equivalente ao codigo fromJson em extensions->Json.ky
    private fun parserJson(json :String):ArrayList<JSONObject>{
        val aList = ArrayList<JSONObject>()
        val array = JSONArray(json)
        for(i in 0..array.length() -1){
            aList.add(array.getJSONObject(i))
         }
        return  aList
    }
}