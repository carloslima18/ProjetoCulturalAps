package br.edu.computacaoifg.todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import org.w3c.dom.Text
import java.util.*


/**
 * Created by Alessandro on 04/10/2017.
 */
class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase") {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            instance = instance ?: MyDatabaseOpenHelper(ctx.applicationContext)
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("usuario", false,
                "_id" to INTEGER + PRIMARY_KEY,
                "email" to TEXT,
                "senha" to INTEGER)

        //db.dropTable("publicacao")
        db.createTable("publicacao", false,
                "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,

                "nome" to TEXT,
                "redesocial" to TEXT,
                "endereco" to TEXT,
                "contato" to TEXT,
                "atvexercida" to TEXT,
                "categoria" to TEXT,

                "img1" to TEXT,
                "img2" to TEXT,
                "img3" to TEXT,
                "img4" to TEXT,

                "longitude" to TEXT,
                "latitude" to TEXT,

                "idUsuario" to INTEGER
               // FOREIGN_KEY("idUsuario", "usuario", "id")
                )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("publicacao")
        // Here you can upgrade tables, as usual
    }
}