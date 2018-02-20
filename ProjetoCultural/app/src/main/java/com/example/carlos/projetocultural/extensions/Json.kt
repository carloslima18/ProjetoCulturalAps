package com.example.carlos.projetocultural.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


//converte o objeto para Json
fun Any.toJson(prettyPrinting:Boolean = false):String{
    val builder = GsonBuilder()
    if(prettyPrinting){
        builder.setPrettyPrinting()
    }
    val json = builder.create().toJson(this)
    return json
}

//Converte Json para objeto
inline fun <reified T> Any.fromJson(json:String):T{
    val type = object :TypeToken<T>(){}.type
    return Gson().fromJson<T>(json,type)
}

