package br.edu.computacaoifg.todolist

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.utils.CameraHelper
import java.nio.file.Path

class ToDoAdapter(context: Context, cursor:Cursor): CursorAdapter(context,cursor,0){

    val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)

    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {
      return LayoutInflater.from(p0).inflate(R.layout.list_row,p2,false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val name =  cursor?.getString(cursor.getColumnIndex("nome"))
        val tipo = cursor?.getString(cursor.getColumnIndexOrThrow("categoria"))
        val endereco = cursor?.getString(cursor.getColumnIndexOrThrow("endereco"))
        val contato = cursor?.getString(cursor.getColumnIndexOrThrow("contato"))
        val redesc = cursor?.getString(cursor.getColumnIndexOrThrow("redesocial"))
        val atvEx = cursor?.getString(cursor.getColumnIndexOrThrow("atvexercida"))
        val img1 = cursor?.getString(cursor.getColumnIndexOrThrow("img1"))
        val img2 = cursor?.getString(cursor.getColumnIndexOrThrow("img2"))
        val img3 = cursor?.getString(cursor.getColumnIndexOrThrow("img3"))
        val img4 = cursor?.getString(cursor.getColumnIndexOrThrow("img4"))
        if(img1 != null) {
            val path1 = Uri.parse(img1)
            val imga = view?.findViewById<ImageView>(R.id.imglistrow1)
            imga?.setImageURI(path1)
        }
        if(img2 != null) {
            val path2 = Uri.parse(img2)
            val imgb = view?.findViewById<ImageView>(R.id.imglistrow2)
            imgb?.setImageURI(path2)
        }
        if(img3 != null){
            val path3 = Uri.parse(img3)
            val imgc = view?.findViewById<ImageView>(R.id.imglistrow3)
            imgc?.setImageURI(path3)
        }
        if(img4 != null){
            val path4 = Uri.parse(img4)
            val imgd = view?.findViewById<ImageView>(R.id.imglistrow4)
            imgd?.setImageURI(path4)
        }

        (view?.findViewById<TextView>(R.id.textViewnome))?.text=name
        (view?.findViewById<TextView>(R.id.textViewtipo))?.text=tipo
        (view?.findViewById<TextView>(R.id.textViewend))?.text=endereco
        (view?.findViewById<TextView>(R.id.textViewcontato))?.text=contato
        (view?.findViewById<TextView>(R.id.textViewredeSoc))?.text=redesc
        (view?.findViewById<TextView>(R.id.textViewAtcEx))?.text=atvEx





       /* if(img1 != null){//burrada
            imga?.setImageBitmap( camera.base64ToBitmap(img1))
        }
        if(img2 != null){
            imgb?.setImageBitmap( camera.base64ToBitmap(img2))
        }
        if(img3 != null){
            imgc?.setImageBitmap( camera.base64ToBitmap(img3))
        }
        if(img4 != null){
            imgd?.setImageBitmap( camera.base64ToBitmap(img4))
        }*/





        //(view?.findViewById<ImageView>(R.id.imglistrow2))?.setImageURI(path2)
        //(view?.findViewById<ImageView>(R.id.imglistrow3))?.setImageURI(path3)
        //(view?.findViewById<ImageView>(R.id.imglistrow4))?.setImageURI(path4)
    }
}
