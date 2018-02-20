package com.example.carlos.projetocultural

/**
 * Created by carlo on 06/02/2018.
 */import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class ModelPub(nome:String, redesocial:String, endereco:String,
               contato:String,atex:String,
               categoria:String,
               lat:String, long:String) {
     var nome = nome
     var redesocial = redesocial
     var endereco = endereco
     var contato = contato
     var atex = atex
     var categoria = categoria
     var lat = lat
     var long = long
}