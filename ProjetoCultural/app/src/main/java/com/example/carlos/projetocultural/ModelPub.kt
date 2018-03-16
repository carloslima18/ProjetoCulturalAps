package com.example.carlos.projetocultural

/**
 * Created by carlo on 06/02/2018.
 */import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/*
@Dao
interface ModelPub {
     @Query("SELECT * FROM carro where id = :arg0")
     fun getById(id: Long): Carro?

     @Query("SELECT * FROM carro")
     fun findAll(): List<Carro>

     @Insert
     fun insert(carro: Carro)

     @Delete
     fun delete(carro: Carro)
}*/