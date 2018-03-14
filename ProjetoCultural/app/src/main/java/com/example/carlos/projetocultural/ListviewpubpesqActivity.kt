package com.example.carlos.projetocultural

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Point
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import kotlinx.android.synthetic.main.activity_listviewpubpesq.*
import kotlinx.android.synthetic.main.content_principal.*


class ListviewpubpesqActivity : AppCompatActivity() {

    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)
    var database: MyDatabaseOpenHelper?=null //para usar o BD
    var todoCursor: Cursor?=null //para guardar e manipular os dados do BD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listviewpubpesq)
            database = MyDatabaseOpenHelper.getInstance(applicationContext) //estancia o BD na variavel para uso com slqLIte
            todoCursor = database!!.writableDatabase.rawQuery("Select * from publicacaop", null) //usa o cursor para armazena a pesquisa do BD
            CA = ToDoAdapter(this, todoCursor!!, "PrincipalActivity")// pega o cursor que ta a consulta com os dados feita na linah de cima, e joga no CA(apapter para ser preenchido a listRow)
            lvPubpesq.adapter = CA// seta o adapter da listView com o CursorAdapter criado(que possui a consulta do banco de dados) e seta na listView (fazendo com que a classe toDoAdapter mapeia os dados)
    }

    override fun onResume() {
        super.onResume()

        //ativa o botao para deletar ou editar a publicação quando clica em alguma item da list view
        lvPubpesq.setOnItemClickListener {
            adapterView, view, i, l -> operacaoFragment(view, lvPubpesq.getAdapter().getItemId(i).toInt(),applicationContext)//lvPubMain.getAdapter().getItemId(i).toInt()
        }

    }



    private fun operacaoFragment(view: View?, idx:Int, context: Context){
        val ft= supportFragmentManager.beginTransaction()
        val fragAnterior = supportFragmentManager.findFragmentByTag(UpdatepubpesqActivity.KEY2)
        if (fragAnterior != null) {
            ft.remove(fragAnterior)
        }
        ft.addToBackStack(null)


        // val intent = Intent(this@ListviewpubpesqActivity, UpdatepubpesqActivity::class.java)
        val bundle= Bundle()//bundle para passagem de parametros(dados a baixo) para outra atv
        bundle.putString("nomelr",(view?.findViewById<TextView>(R.id.nomelr))?.text.toString())
        bundle.putString("categorialr",(view?.findViewById<TextView>(R.id.categorialr))?.text.toString())
        bundle.putString("enderecolr",(view?.findViewById<TextView>(R.id.enderecolr))?.text.toString())
        bundle.putString("redesclr",(view?.findViewById<TextView>(R.id.redesociallr))?.text.toString())
        bundle.putString("contatolr",(view?.findViewById<TextView>(R.id.contatolr))?.text.toString())
        bundle.putString("emaillr",(view?.findViewById<TextView>(R.id.emaillr))?.text.toString())

        bundle.putString("pesquisadorlr",(view?.findViewById<TextView>(R.id.pesquisador))?.text.toString())
        bundle.putString("cnpjlr",(view?.findViewById<TextView>(R.id.cnpj))?.text.toString())
        bundle.putString("anoiniciolr",(view?.findViewById<TextView>(R.id.anoinicio))?.text.toString())
        bundle.putString("recursolr",(view?.findViewById<TextView>(R.id.recurso))?.text.toString())
        bundle.putString("representacaolr",(view?.findViewById<TextView>(R.id.representacao))?.text.toString())

        bundle.putString("atvexlr",(view?.findViewById<TextView>(R.id.atvexlr))?.text.toString())
        bundle.putString("campo5lr",(view?.findViewById<TextView>(R.id.campo5lr))?.text.toString())
        bundle.putString("campo1lr",(view?.findViewById<TextView>(R.id.campo1lr))?.text.toString())
        bundle.putString("campo2lr",(view?.findViewById<TextView>(R.id.campo2lr))?.text.toString())
        bundle.putString("campo3lr",(view?.findViewById<TextView>(R.id.campo3lr))?.text.toString())
        bundle.putString("campo4lr",(view?.findViewById<TextView>(R.id.campo4lr))?.text.toString())
        bundle.putInt("idx",idx)



        val dialog = UpdatepubpesqActivity()//estancia o fragment para passar os dados do bundle
        dialog.arguments = bundle
        dialog.show(ft, UpdatepubpesqActivity.KEY2)//chama o fragment
    }





}
