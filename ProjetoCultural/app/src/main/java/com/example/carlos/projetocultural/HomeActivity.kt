package com.example.carlos.projetocultural

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.AndroidUtils
import org.jetbrains.anko.toast
import com.example.carlos.projetocultural.domain.GetRequisitaPub
import org.json.JSONObject
import kotlinx.android.synthetic.main.content_home.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity() {

    var database: MyDatabaseOpenHelper?=null
    val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1 //variaveis para permissões
    val MY_WRITE_EXTERNAL_STORAGE = 1
    val MY_READ_EXTERNAL_STORAGE = 1
    val MY_MANEGER_DOCUMENT = 1
    val MY_INTERNET = 1
    val MY_ACCESS_FINE_LOCATION = 1
    var CL:Listadapter?= null
    val STATE_LIST = "State Adapter Data"


    private var OFFSET = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState!=null){
            val mListstate = savedInstanceState.getParcelable<Parcelable>(STATE_LIST)
            lvPubFirst.onRestoreInstanceState(mListstate)
        }

        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        lvPubFirst.adapter=CL


        lvPubFirst.setOnScrollListener(object : PubAdapter() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                OFFSET += 2
               // moreData()
            }
        })







        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val extras = intent.extras
        GetPermission()
        if(extras == null) {
            PreenchePubFirst("&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1")
        }else {
            val param = extras.getString("param")
            PreenchePubFirst(param)
        }

        if(AndroidUtils.isNetworkAvailable(applicationContext)) {
            lvPubFirst.setOnItemClickListener { parent, view, position, id ->
                val idx = view.findViewById<TextView>(R.id.idpub).text
                val long = view.findViewById<TextView>(R.id.textViewLog).text
                val lati = view.findViewById<TextView>(R.id.textViewLat).text
                val nome = view.findViewById<TextView>(R.id.textViewnomeM).text
                val atvex = view.findViewById<TextView>(R.id.textViewAtcExM).text
                //viewPubFragment(savedInstanceState)
                val intent = Intent(applicationContext, actViewPub::class.java)
                intent.putExtra("idpub", idx.toString())
                intent.putExtra("lat", long.toString())
                intent.putExtra("log", lati.toString())
                intent.putExtra("nome", nome.toString())
                intent.putExtra("atvex", atvex.toString())
                startActivity(intent)
                // toastx()
            }
        }else{
            toast("verifique sua conexão")
        }
    }



    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putParcelable(STATE_LIST,lvPubFirst.onSaveInstanceState())
    }


    override fun onResume() {
        super.onResume()
        FAB_att.setOnClickListener(){
            PreenchePubFirst("&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1")
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onPause() {
        super.onPause()
    }

    fun PreenchePubFirst(parametro:String){
        //comentadox
        if(AndroidUtils.isNetworkAvailable(applicationContext) ) {
            toast("buscando Publicações")
            CL?.clear()
            CL?.notifyDataSetChanged()
            val listV: ListView = findViewById<ListView>(R.id.lvPubFirst) as ListView
            listV.adapter = CL
            val getp = GetRequisitaPub(applicationContext,listV,parametro)
            getp.execute()
        }else{
            toast("sem conecção")
        }
    }

    fun GetPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), MY_INTERNET)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.MANAGE_DOCUMENTS), MY_MANEGER_DOCUMENT)
        }
    }

}
