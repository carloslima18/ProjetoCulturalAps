package br.edu.computacaoifg.todolist
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import org.w3c.dom.Text
import java.util.*

/**
 * Created by carlo on 04/10/2017.
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
        //db.dropTable("publicacao")
        db.createTable("publicacao2", false,
                "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                "nome" to TEXT,
                "redesocial" to TEXT,
                "endereco" to TEXT,
                "contato" to TEXT,
                "email" to TEXT,
                "atvexercida" to TEXT,
                "categoria" to TEXT,
                "campo1" to TEXT,
                "campo2" to TEXT,
                "campo3" to TEXT,
                "campo4" to TEXT,
                "campo5" to TEXT,
                "img1" to TEXT,
                "img2" to TEXT,
                "img3" to TEXT,
                "img4" to TEXT,
                "longitude" to TEXT,
                "latitude" to TEXT
                )

        db.createTable("publicacaop", false,
                "_id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                "nome" to TEXT,
                "redesocial" to TEXT,
                "endereco" to TEXT,
                "contato" to TEXT,
                "email" to TEXT,
                "atvexercida" to TEXT,
                "categoria" to TEXT,
                "anoinicio" to TEXT,
                "cnpj" to TEXT,
                "representacao" to TEXT,
                "recurso" to TEXT,
                "longitude" to TEXT,
                "latitude" to TEXT,
                "pesquisador" to TEXT,
                "img1" to TEXT,
                "img2" to TEXT,
                "img3" to TEXT,
                "img4" to TEXT,
                "campo1" to TEXT,
                "campo2" to TEXT,
                "campo3" to TEXT,
                "campo4" to TEXT,
                "campo5" to TEXT
                )


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable("publicacao2")
        //db.dropTable("pubpesq")
        // Here you can upgrade tables, as usual
    }
}