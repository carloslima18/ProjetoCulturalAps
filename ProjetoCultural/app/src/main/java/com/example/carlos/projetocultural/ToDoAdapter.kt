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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.utils.CameraHelper
import java.nio.file.Path

class ToDoAdapter(context: Context, cursor:Cursor,classe:String): CursorAdapter(context,cursor,0){
    val classe = classe
    val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)

    override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {
      return LayoutInflater.from(p0).inflate(R.layout.list_row,p2,false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val name =  cursor?.getString(cursor.getColumnIndex("nome"))
        val categoria = cursor?.getString(cursor.getColumnIndexOrThrow("categoria"))
        val endereco = cursor?.getString(cursor.getColumnIndexOrThrow("endereco"))
        val contato = cursor?.getString(cursor.getColumnIndexOrThrow("contato"))
        val redesc = cursor?.getString(cursor.getColumnIndexOrThrow("redesocial"))
        val atvEx = cursor?.getString(cursor.getColumnIndexOrThrow("atvexercida"))

        val email = cursor?.getString(cursor.getColumnIndexOrThrow("email"))
        val campo1 = cursor?.getString(cursor.getColumnIndexOrThrow("campo1"))
        val campo2 = cursor?.getString(cursor.getColumnIndexOrThrow("campo2"))
        val campo3 = cursor?.getString(cursor.getColumnIndexOrThrow("campo3"))
        val campo4 = cursor?.getString(cursor.getColumnIndexOrThrow("campo4"))
        val campo5 = cursor?.getString(cursor.getColumnIndexOrThrow("campo5"))


        if(classe == "FormActivity"){
            val anoinicio = cursor?.getString(cursor.getColumnIndexOrThrow("anoinicio"))
            val cnpj = cursor?.getString(cursor.getColumnIndexOrThrow("cnpj"))
            val recurso = cursor?.getString(cursor.getColumnIndexOrThrow("recurso"))
            val representacao = cursor?.getString(cursor.getColumnIndexOrThrow("representacao"))
            val pesquisador = cursor?.getString(cursor.getColumnIndexOrThrow("pesquisador"))
            (view?.findViewById<TextView>(R.id.anoiniciolr))?.text=anoinicio
            (view?.findViewById<TextView>(R.id.cnpjlr))?.text=cnpj
            (view?.findViewById<TextView>(R.id.representacaolr))?.text=representacao
            (view?.findViewById<TextView>(R.id.recursolr))?.text=recurso
            (view?.findViewById<TextView>(R.id.pesquisadorlr))?.text=pesquisador
       }

        val img1 = cursor?.getString(cursor.getColumnIndexOrThrow("img1"))
        val img2 = cursor?.getString(cursor.getColumnIndexOrThrow("img2"))
        val img3 = cursor?.getString(cursor.getColumnIndexOrThrow("img3"))
        val img4 = cursor?.getString(cursor.getColumnIndexOrThrow("img4"))
        if(img1 != null) {
            val path1 = Uri.parse(img1)
            val imga = view?.findViewById<ImageView>(R.id.img1lr)
            //imga?.setImageURI(path1)
            Glide.with(context).load(path1).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(imga)
        }
        if(img2 != null) {
            val path2 = Uri.parse(img2)
            val imgb = view?.findViewById<ImageView>(R.id.img2lr)
            Glide.with(context).load(path2).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgb)
            //imgb?.setImageURI(path2)
        }
        if(img3 != null){
            val path3 = Uri.parse(img3)
            val imgc = view?.findViewById<ImageView>(R.id.img3lr)
            Glide.with(context).load(path3).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgc)
         //   imgc?.setImageURI(path3)
        }
        if(img4 != null){
            val path4 = Uri.parse(img4)
            val imgd = view?.findViewById<ImageView>(R.id.img4lr)
            Glide.with(context).load(path4).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgd)
            //imgd?.setImageURI(path4)
        }

        (view?.findViewById<TextView>(R.id.nomelr))?.text=name
        (view?.findViewById<TextView>(R.id.categorialr))?.text=categoria
        (view?.findViewById<TextView>(R.id.enderecolr))?.text=endereco
        (view?.findViewById<TextView>(R.id.contatolr))?.text=contato
        (view?.findViewById<TextView>(R.id.redesociallr))?.text=redesc
        (view?.findViewById<TextView>(R.id.atvexlr))?.text=atvEx

        (view?.findViewById<TextView>(R.id.emaillr))?.text=email
        (view?.findViewById<TextView>(R.id.campo1lr))?.text=campo1
        (view?.findViewById<TextView>(R.id.campo2lr))?.text=campo2
        (view?.findViewById<TextView>(R.id.campo3lr))?.text=campo3
        (view?.findViewById<TextView>(R.id.campo4lr))?.text=campo4
        (view?.findViewById<TextView>(R.id.campo5lr))?.text=campo5




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
