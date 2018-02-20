package com.example.carlos.projetocultural

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_act_view_pub.*
import kotlinx.android.synthetic.main.content_act_view_pub.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.ArrayList

class actViewPub : AppCompatActivity(), OnMapReadyCallback {

    var id:String?=null
    var camera = CameraHelper()
    var mapView : MapView?=null
    var i = 1
    var latitude:Double?= null // variaveis para pegar via intentPut extra a long e lat enviadas..
    var longitude: Double?= null
    var bitmap1 : String = ""
    var nome : String = ""
    var atvex : String = ""
    var bitmap2 : String = ""
    var bitmap3 : String = ""
    var bitmap4 : String = ""

    val mappub = MapPub()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_view_pub)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
         mapViewPub.visibility = View.INVISIBLE

        //pega dados do intent
        val extras = intent.extras
        id = extras.getString("idpub")
        latitude = extras.getDouble("lat")
        longitude = extras.getDouble("log")
        nome = extras.getString("nome")
        atvex = extras.getString("atvex")

        //preenche os itens da view dessa activity, com os dados capturados do webService
    //    if(AndroidUtils.isNetworkAvailable(applicationContext)) {
            getViewPub()
    //    }else{
     //       toast("sem coneçcão")
     //   }
        ////para o MAPview
        mapView = findViewById<MapView>(R.id.mapViewPub) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

    }


    //para visualizar os dados a partir do click de uma view da listView na atividade principal
    fun getViewPub(){
        val texto = findViewById<TextView>(R.id.textvInfoPub) as TextView
        val enderecoPub = findViewById<TextView>(R.id.enderecoPub) as TextView
        val titulo = findViewById<TextView>(R.id.tituloPub) as TextView
        val img = findViewById<ImageView>(R.id.imgViewPub) as ImageView
        val mapv = findViewById<MapView>(R.id.mapViewPub) as MapView
        var listItems : ArrayList<JSONObject> = arrayListOf()
        val dialog = ProgressDialog.show(this, "Um momento","buscando dados",false,true)

        Thread{
          val url =  "http://192.168.43.14/geolocation/position/$id?_format=json&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1,img2,img3,img4"
          listItems = pubService.getPub2(url)
            runOnUiThread {
                if(listItems.size != 0) {
                    val nome = listItems[0].getString("nome")
                    val redesc = listItems[0].getString("redesocial")
                    val end = listItems[0].getString("endereco")
                    val contato = listItems[0].getString("contato")
                    val avte = listItems[0].getString("atvexercida")
                    val cat = listItems[0].getString("categoria")
                    latitude = listItems[0].getString("latitude").toDouble()
                    longitude = listItems[0].getString("longitude").toDouble()
                    bitmap1 = listItems[0].getString("img1")
                    bitmap2 = listItems[0].getString("img2")
                    bitmap3 = listItems[0].getString("img3")
                    bitmap4 = listItems[0].getString("img4")

                    titulo.text = "Nome :" + nome + "\n "

                    enderecoPub.text = "endereço: " + end + "\n "

                    texto.text = "rede social: " + redesc + "\n" + "contato: " + contato + "\n" + "atividade exercida: " + avte + "\n" + "Categoria: " + cat + " " + "\n"

                    // val uri1 = camera.base64forUri(applicationContext,bitmapa)
                    if (bitmap1 != "") {
                        img.setImageBitmap(camera.base64ForBitmap2(bitmap1))
                        // Glide.with(applicationContext).load(uri1).asBitmap().override(100, 100).into(img)
                    }
                    dialog.dismiss()
                    mapViewPub.visibility = View.VISIBLE
                }else{
                    toast("tente novamente")
                    finish()
                }
            }
        }.start()

    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState) //padrão para o mapView
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart();//padrão para o mapView
    }
    override fun onStop() {
        super.onStop()
        mapView?.onStop();//padrão para o mapView
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()//padrão para o mapView
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()//padrão para o mapView
    }
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume();


        buttonAvl.setOnClickListener {
            val idx = id
            //viewPubFragment(savedInstanceState)
            val intent = Intent(applicationContext, AvaliacaoActivity::class.java)
            intent.putExtra("idpub", idx.toString())
            startActivity(intent)
        }


        mapViewPub.setOnClickListener {
            val intent = Intent(applicationContext, MapPub::class.java)
        }

        //para atualizar e fica trocando de imagem a cada click na imagem
        nextFtPub.setOnClickListener {
            val img = findViewById<ImageView>(R.id.imgViewPub) as ImageView
            if(i > 4){i = 1}
            if(i == 1 && bitmap1 != ""){
                img.setImageBitmap(camera.base64ForBitmap2(bitmap1))
            }else if(i == 2 && bitmap2 != ""){
                img.setImageBitmap(camera.base64ForBitmap2(bitmap2))
            }
            else if(i == 3 && bitmap3 != ""){
                img.setImageBitmap(camera.base64ForBitmap2(bitmap3))
            }
            else if(i ==4 && bitmap4 != ""){
                img.setImageBitmap(camera.base64ForBitmap2(bitmap4))
            }
            i++
        }
    }


    // função obrigatoria para a extensão "OnMapReadyCallback" feita nessa atv para mostrar a coordenada no mapView
    override fun onMapReady(googleMap: GoogleMap?) {
        /*já mostra as coordenadas no mapView ao abrir a actividade, que foram passadas
        //pela PrincipalActivity por parametro (essas coordenadas) foram adquiridas
        //automaticamente la na Prin.Act referente ao local atual da pessoa quando abre o app(para caso for add um publicação aq)*/
        mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)

        //aq só faz a atualização do marcador, dps que a pessoa confirma a atualização no mapa pela outra função responsavel, (quando clica no MapView)*/
        googleMap?.setOnMapClickListener(){
            mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)
        }
    }
}
