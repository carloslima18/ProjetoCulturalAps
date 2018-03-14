package com.example.carlos.projetocultural

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper

import com.google.android.gms.maps.OnMapReadyCallback


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import android.text.Html
import android.view.View
import android.widget.*
import com.example.carlos.projetocultural.extensions.setupToolbar
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map_pub.*
import kotlinx.android.synthetic.main.content_avaliacao.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


class MapPub : AppCompatActivity(), OnMapReadyCallback{

    //USE O CONTENT PARA CADA LOCAL, VC USA, O SE OE SENAO PARA USAR OU NAO USAR DETERMINADOS METODOS
    //private var mapFragment: SupportMapFragment? = null
    //var todoCursor: Cursor?=null
    //public var nometest:Array<String>? =null
    private lateinit var mMap: GoogleMap
    var marker : Marker ?= null
    var database: MyDatabaseOpenHelper?=null
    var mostrar:String ?=null
    var intentEnviaBroadcast:Intent?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_pub)
        setupToolbar(R.id.toolbar)




        //setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        //Obtenha SupportMapFragment e receba notificações quando o mapa estiver pronto para ser usado.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        intentEnviaBroadcast = Intent(loc_receiver) //envia o intent para o broadcast
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        //botao para voltar(fechar a tela do mapa e retorna para atividade anterior)
        voltarMap.setOnClickListener(){finish()}
     }


    override fun onMapReady(mMap: GoogleMap) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        val mHashMap = HashMap<Marker, Int>()
        // mMap.setMyLocationEnabled(true); //para localização atual
        database = MyDatabaseOpenHelper.getInstance(applicationContext)

        //pegas os dados enviados por intentes de outras atividades quando faz requisição dessa atividade
        val extras = intent.extras
        val longitude = extras.getStringArray("longitude")
        val latitude  = extras.getStringArray("latitude")
        val nome = extras.getStringArray("nome")
        val atvex = extras.getStringArray("atvex")
        val idpub = extras.getStringArray("idpub")
        mostrar = extras.getString("mostrar")   //determina se vai executar certas funções desse metodo (ex, colocamos uma cond. para editar o marcador, ou n),, depende dessa variavel

        //os valores passados ocmo long lat, e os demais, podem estar em um array, sendo assim pode conter mais de um valor
        //.. aq se pega o "tamanho do array" passado, para caso tiver, mostre mais marcações no mapa com a iteração
        val tam = longitude!!.size
        var i = 0;

        try {

            while (i < tam) {  //pega to_do o conteudo passado das coordenadas e coloca no mapa
                val sydney = LatLng(latitude?.get(i)!!.toDouble(), longitude[i].toDouble()) //coordenadas
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f));// trata o quão perto mostra a coordenada             //...mMap.animateCamera(CameraUpdateFactory.zoomTo(20f), 20000, null);
                val marker = mMap.addMarker(MarkerOptions().position(sydney).title(nome?.get(i)).snippet(atvex.get(i)).flat(true).rotation(245f))
                if(idpub != null) {
                    mHashMap.put(marker, idpub.get(i).toInt())
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)) //esqueci
                //definees propriedades da posicao da camera
                val cameraPosition = CameraPosition.builder().target(sydney).zoom(13f).bearing(90f).build();
                // Animate the change in camera view over 2 seconds
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                marker?.showInfoWindow()
                marker?.hideInfoWindow()
                i++;
            }



            //para fazer as caixas de texto que mostra ao clicar, ou.., ou poder excluir uma marcação
            mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoContents(marker: Marker): View {
                    val tv = TextView(this@MapPub)
                    tv.text = Html.fromHtml("<b><font color=\"#d32f2f\">" + marker.title + ":</font></b> " + marker.snippet)
                    return tv
                }
                override fun getInfoWindow(marker: Marker): View {

                    val ll = LinearLayout(this@MapPub)
                    ll.setPadding(10, 10, 10, 10)
                    ll.setBackgroundColor(Color.LTGRAY)
                    val tv = TextView(this@MapPub)
                    tv.text = Html.fromHtml("<b><font color=\"#311b92\">" + marker.title + ":</font></b> " + marker.snippet)
                    ll.addView(tv)

                    //pode confirmar e enviar a localização do marcador
                    if(mostrar == "atualiza") {
                        alert("Está localização está correta?") {
                            title = "Atenção"
                            negativeButton("Sim") {
                                val newLat = marker.position.latitude
                                val newLog = marker.position.longitude
                                intentEnviaBroadcast?.putExtra("latMap", newLat.toString())
                                intentEnviaBroadcast?.putExtra("logMap", newLog.toString())
                                //ATENTE QUE SO EXECUTA ESSE CODIGO SE A VAR 'MOSTRAR" ESTIVER SETADA COMO ATUALIZAE
                                sendBroadcast(intentEnviaBroadcast)  //AQ ENVIA VIA BROADCAST QUANDO O USUARIO MUDA A POSIÇÃO
                                toast("local confirmado")
                            }
                            positiveButton("Remarcar local") {
                                marker.remove()
                                toast("escolha outro local, tocando tela e segurando, a posição no mapa")
                            }
                        }.show()
                    }
                    return ll
                }
            })

            mMap.setOnCameraChangeListener { cameraPosition ->
                // Log.i("Scritp", "setOnCameraChangeListener")
                if (marker != null) {
                    marker?.remove()
                }
                //quando a camera se meche ele coloca um marcador!!! com o codigo abaixo
                /*var latlng = LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude)
                  mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13f));
                 mMap?.addMarker(MarkerOptions().position(latlng).title("carlos").snippet("informaçao adicional").flat(true).rotation(245f))
                 mMap?.moveCamera(CameraUpdateFactory.newLatLng(latlng)) */
            }

            if(mostrar == "atualiza") {
                mMap.setOnMapLongClickListener() { LatLng ->
                    Log.i("Script", "setOnMapClickListener()")
                    if (marker != null) {
                        marker?.remove()
                    }
                    val latlng = LatLng(LatLng.latitude, LatLng.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
                    mMap.addMarker(MarkerOptions().position(latlng).title("novo Local").snippet("clique em confirmar").flat(true)
                            .rotation(245f))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                }
            }

            //para quando vc clica no marcador
            mMap.setOnMarkerClickListener { marker ->
               // toast("clicado no marcador")
                false
            }

            //realizar algo quando toca na etiqueta (caixa de texto) sobre a marcação
            mMap.setOnInfoWindowClickListener { marker ->

                val intent = Intent(applicationContext, actViewPub::class.java)
                val t =mHashMap.get(marker)
                if(idpub != null) {
                    intent.putExtra("idpub", mHashMap.get(marker).toString())
                }
               // intent.putExtra("lat", marker.position.)
               // intent.putExtra("log", longitude.get(0))
                intent.putExtra("nome", marker.title.toString())
                intent.putExtra("atvex", marker.snippet.toString())
                startActivity(intent)
                toast("Seu local")
            }

        }catch (e:Exception){
            Toast.makeText(applicationContext, "publicação sem cordenadas", Toast.LENGTH_SHORT).show()
        }
    }



    public fun atualizaCoordenadasNoMapView(googleMap: GoogleMap?,latitude :Double?,longitude:Double?,title: String, subtexto:String){
        val latlng = LatLng(latitude!!.toDouble(),longitude!!.toDouble())
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
        googleMap?.addMarker(MarkerOptions().position(latlng).title(title).snippet(subtexto).flat(true)
                .rotation(245f))
    }

    companion object {
        val loc_receiver = "APPMAP"
    }

    // nao utilizado pq n deu certo
    public fun customAddMarker(latlng: LatLng, title:String,snippet:String){
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13f));
        mMap?.addMarker(MarkerOptions().position(latlng).title("carlos").snippet("informaçao adicional").flat(true)
                .rotation(245f))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latlng))
    }
}
