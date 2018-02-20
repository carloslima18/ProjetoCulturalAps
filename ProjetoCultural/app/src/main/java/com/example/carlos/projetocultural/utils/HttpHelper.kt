package com.example.carlos.projetocultural.utils
import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

//classe para tratar webService/requisições, envios etc...
object HttpHelper{
    private val TAG = "http"
    private val LOG_ON = true
    val JSON = MediaType.parse("application/json; charset=utf-8")
    var client = OkHttpClient()
    var build = OkHttpClient.Builder().connectTimeout(5,TimeUnit.MINUTES)
            .writeTimeout(5,TimeUnit.MINUTES).readTimeout(5,TimeUnit.MINUTES)


    //GET
    fun get(url:String):String{
      //  log("HttpHelper.get: $url")
        val request = Request.Builder().url(url).get().build()
        return getJson(request)
    }

    //POST com JSON
    fun post(url: String,json:String):String{
        log("HttpHelper.post: $url > $json")
        val body = RequestBody.create(JSON,json)
        val request = Request.Builder().url(url).post(body).build()
        return getJson(request)
    }


    //Lê a resposta do servidor no formato JSON
    private fun getJson(request: Request):String{
        client = build.build()
        val response = client.newCall(request).execute()
        val responseBody = response.body()
        if(responseBody != null){
            val json = responseBody.string()
            //log("  <<: $json")
            return json
        }
        throw IOException("erro ao fazer a requisição")
    }

    private fun log(s: String){
        if(LOG_ON){
            Log.d(TAG,s)
        }
    }

}