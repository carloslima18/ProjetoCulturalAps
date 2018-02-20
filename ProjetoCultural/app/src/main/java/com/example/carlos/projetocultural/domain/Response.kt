package com.example.carlos.projetocultural.domain

/**
 * Created by carlo on 07/02/2018.
 */
//para qunado envia uma publicação do webService, pegar a resposta da req.
data class Response (val id:Long,val status:String,val msg:String,val url:String) {
    fun isOk() = "OK".equals(status, ignoreCase = true)
}