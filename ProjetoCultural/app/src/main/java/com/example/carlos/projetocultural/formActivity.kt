package com.example.carlos.projetocultural

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor


import org.jetbrains.anko.db.*

import android.graphics.Bitmap
import android.graphics.Camera
import android.media.Image
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.extensions.toast

import com.example.carlos.projetocultural.utils.CameraHelper
import com.example.carlos.projetocultural.utils.ActionsForMaps
import com.example.carlos.projetocultural.utils.AndroidUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.act
import org.jetbrains.anko.db.*
import java.util.*
import java.util.Arrays.asList




class FormActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , OnMapReadyCallback, AdapterView.OnItemSelectedListener  {

    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)
    val camera = CameraHelper()
   // var database: MyDatabaseOpenHelper?=null //para usar o BD

    var googleApiClient : GoogleApiClient ?= null
    var latitude: Double ?= 0.0//variaveis para as coordenadas
    var longitude: Double ?= 0.0
    var permission = false  //variavel para verificar permissão
    val REQUEST_PESMISSION_GPS=1 //para GPS
    val mappub = MapPub()
    var mapView : MapView?=null

    private val REQUEST_IMAGE_CODE = 1888; //variaveis obrigatorias para parametro da funcao "startActivityForResult"
    var numImgx:Int ?= 0
    val PICK_FROM_FILE = 2;
    var base64_1:String?=null  //variaveis que recebe a string BASE64 das imagens para..
    var base64_2:String?=null  //..salvar no banco de dados
    var base64_3:String?=null
    var base64_4:String?=null
    var imgC1:ImageView ?= null
    var imgC2:ImageView ?= null
    var imgC3:ImageView ?= null
    var imgC4:ImageView ?= null
    var contextx: Context?=null

    var actionsFormaps = ActionsForMaps

    var database: MyDatabaseOpenHelper?=null //para usar o BD

    /* inner class Curso(nome: String, descricao: String){
         private var nome: String? = nome
         private var descricao: String? = descricao
         override fun toString(): String {
             return "Curso: " + nome + " Descrição: " + descricao
         }

     }

     private fun todosOsCursos(): List<Curso> {
         return ArrayList(Arrays.asList(
                 Curso("Java", "básico de Java"),
                 Curso("HTML e CSS", "HTML 5 e suas novidades")))
     }
 */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add)


        val addcampop = findViewById<Button>(R.id.addcampolr) as Button
        addcampop.visibility = View.VISIBLE

        database = MyDatabaseOpenHelper.getInstance(applicationContext) //estancia o BD na variavel para uso com slqLIte

       /* val lvform = findViewById<ListView>(R.id.lvform) as ListView

        val curso:List<Curso> = todosOsCursos()

        val adapterform:ArrayAdapter<Curso> = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,curso)

        lvform.adapter = adapterform


        //webviewget() */

        //Adicionando o Location Service API (do google) na variavel para ser usada (PARA PEGAR A POSIÇÃO ATUAL DO USUARIO)
        //chamando a classe, e ativando para que envie uma mensagem de broadcast, que vai ser passado a posição do cliente e pegado nessa atividad logo quando executar essa linha (vai ser pego com a função de briadcast)
       // googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()

        //recebe a instancia do "obj" mapView que se encontra no layout desse fragment
        mapView = findViewById<MapView>(R.id.figMapAdda) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        val spinner = findViewById<Spinner>(R.id.spinner) as Spinner
        //  var button = view?.findViewById<Button>(R.id.button)
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        val categories = ArrayList<String>();
        categories.add("escola");
        categories.add("praça");
        categories.add("museus");
        categories.add("teatro");
        categories.add("feiras");
        categories.add("outros");
        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(dataAdapter);

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // On selecting a spinner item
        val item = parent?.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent?.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState) //padrão para o mapView
    }
    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
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
        try {
            unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        super.onPause()

        mapView?.onPause()

    }
    //para estarta a função onConnected quando abrir a atv para assim pegar as coordenadas
    override fun onStart() {
        super.onStart()
        if (googleApiClient != null) {
            googleApiClient?.connect()
        }
        mapView?.onStart();//padrão para o mapView
    }
    //função para conectar no serviço do GPS para obter a posição atual do usuario(lat e long)
    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //verifica se o gps ta ligado, se n tiver vai da problema e cair no catch
            try {//pega a latitude e longitude atual da pessoa para assim enviar quando for adicionar uma publicação já tiver os dados
                val lastLocation  = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                latitude = lastLocation.latitude //
                longitude = lastLocation.longitude
            }catch (e:Exception){
                Toast.makeText(applicationContext, "É necessario ligar o GPS", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //broadcast vai ser usado quando ativar o serviço (que foi ativado no oncrate para receber as coordenadas do usuario na hora que estarta essa atividade)
    //OBSERVAÇÃO::::::::::::;; verificar o unreceiver no onPause
    //atualiza as variaveis com as coordenadas novas, selecionadas pelo usuario la no atv MapPub
    inner class broadcastReceive:BroadcastReceiver(){
        override fun onReceive(context: Context?,intent: Intent?){
            latitude = intent?.getStringExtra("latMap")?.toDouble()
            longitude = intent?.getStringExtra("logMap")?.toDouble()
            //          context?.unregisterReceiver(broadcastReceive())
        }
    }
    //para verificação de permissão de acesso ao GPS
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_PESMISSION_GPS -> {
                if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permission=true
                }
            }
        }
    }
    //tbm por conta do google map
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // função obrigatoria para a extensão "OnMapReadyCallback" feita nessa atv para mostrar a coordenada no mapView
    override fun onMapReady(googleMap: GoogleMap?) {
        /*já mostra as coordenadas no mapView ao abrir a actividade, que foram passadas
        //pela PrincipalActivity por parametro (essas coordenadas) foram adquiridas
        //automaticamente la na Prin.Act referente ao local atual da pessoa quando abre o app(para caso for add um publicação aq)*/
        mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,"newPub","subtexto")

        //aq só faz a atualização do marcador, dps que a pessoa confirma a atualização no mapa pela outra função responsavel, (quando clica no MapView)*/
        googleMap?.setOnMapClickListener(){
            mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,"newPub","subtexto")
        }
    }










    override fun onResume() {
        super.onResume()

        /*fica esperando o brosCast quando a função que atualiza a coordenada no mapView na fragment e usada, e retorna
  //as novas coordenadas que o usuario selecionou no mapa*/
        registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))
        //padrao
        mapView?.onResume();

      /*  questionario.setOnClickListener {
            setContentView(R.layout.fragment_add)
            anoinicioa.visibility = View.VISIBLE
            cnpja.visibility = View.VISIBLE
            representacaoa.visibility = View.VISIBLE
            recurso.visibility = View.VISIBLE
            campo1layoutlr.visibility = View.VISIBLE
            campo2layoutlr.visibility = View.VISIBLE
            campo3layoutlr.visibility = View.VISIBLE
            campo4layoutlr.visibility = View.VISIBLE
            campo5layoutlr.visibility = View.VISIBLE
        } */

        val act = this
        val img1a = findViewById<ImageView>(R.id.img1a) as ImageView
        val img2a = findViewById<ImageView>(R.id.img2a) as ImageView
        val img3a = findViewById<ImageView>(R.id.img3a) as ImageView
        val img4a = findViewById<ImageView>(R.id.img4a) as ImageView
        img1a.setOnClickListener {
            camera.tirafoto(1,act,null,img1a,img2a,img3a,img4a)
            numImgx = 1
        }
        img2a.setOnClickListener {
            camera.tirafoto(2,act,null,img1a,img2a,img3a,img4a)
            numImgx = 2
        }
        img3a.setOnClickListener {
            camera.tirafoto(3,act,null,img1a,img2a,img3a,img4a)
            numImgx = 3
        }
        img4a.setOnClickListener {
            camera.tirafoto(4,act,null,img1a,img2a,img3a,img4a)
            numImgx = 4
        }

        buttonSalvara.setOnClickListener {
            onClickCreate()
        }


        var addcampo = 1
        addcampolr.setOnClickListener {
            when(addcampo){
                1 -> {
                    campo1layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                2 -> {
                    campo2layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                3 -> {
                    campo3layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                4 -> {
                    campo4layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
                5 -> {
                    campo5layoutlr.visibility = View.VISIBLE
                    addcampo++
                }
            }

        }


        /*vizualizar localização da publicação no mapa, e da a opção de vc adicionar no mapa outra coordenada caso o usuario n esteja no local
        //.. da publicacao, quando for adicionar a mesma*/
        buttonAddMapa.setOnClickListener(){
            //envia a localização da onde a pessoa esta para o MapPub e da a opção caso ela queira mudar o marcador
            val intent = Intent(applicationContext, MapPub::class.java)
            //val locations:MutableList<String> = mutableListOf(longitude.toString(),latitude.toString())
            val longitudex:List<String> ?= mutableListOf(longitude.toString())
            val latitudex:List<String> ?= mutableListOf(latitude.toString())
            val nomex:List<String> ?= mutableListOf("nova localização")
            val atvex:List<String> ?= mutableListOf("nova publicação")
            intent.putExtra("mostrar","atualiza")//para enviar os dados via intent
            intent.putExtra("longitude",longitudex?.toTypedArray())
            intent.putExtra("latitude",latitudex?.toTypedArray())
            intent.putExtra("nome",nomex?.toTypedArray())
            intent.putExtra("atvex",atvex?.toTypedArray())
            startActivity(intent)//estarta a atividade MapPub que mostra o mapa e da opcao da pessoa troca o marcador
        }

    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        camera.supportForOnActivityResult(data,resultCode,requestCode,0,"add",null)//0 pq só vai ter que passar o valor do id, se for em caso de update/alterar o dado, e add, pq quando é no caso tbm de alteração..
        //precisa mudar o caminho na classe camera!
    }



   // var database:MyDatabaseOpenHelper?=null
    //tem como vc chamar a funcao savePubpesq la da camera...
    fun onClickCreate(){
        val nome = nomea.text.toString()
        val redesocial = redesociala.text.toString()
        val endereco = enderecoa.text.toString()
        val contato = contatoa.text.toString()
        val email = emaila.text.toString()
        val atvexercida = atvexa.text.toString()
        val categoria = spinner.selectedItem.toString()
        val anoinicio = anoinicio.text.toString()
        val cnpj = cnpj.text.toString()
        val representacao = representacao.text.toString()
        val recurso = recurso.text.toString()
        val pesquisador = "???????"

        //val aprovado = categoriaa.aprovadoa.toString()
        val campo1 = campo1a.text.toString()
        val campo2 = campo2a.text.toString()
        val campo3 = campo3a.text.toString()
        val campo4 = campo4a.text.toString()
        val campo5 = campo5a.text.toString()

       val activity = act as FormActivity
       AndroidUtils.savePubpesq(CA,database,nome,redesocial,endereco,contato,email,atvexercida,categoria,campo1,campo2,campo3,campo4,campo5,longitude.toString(),latitude.toString(),pesquisador,anoinicio,cnpj,representacao,recurso,
               camera.base64_1.toString(),camera.base64_2.toString(),camera.base64_3.toString(),camera.base64_4.toString())

      /* activity.database?.use{
            insert("publicacaop",
                    "nome" to nome,
                    "redesocial" to redesocial,
                    "endereco" to endereco,
                    "contato" to contato,
                    "email" to email,
                    "atvexercida" to atvexercida,
                    "categoria" to categoria,
                    "anoinicio" to anoinicio,
                    "cnpj" to cnpj,
                    "representacao" to representacao,
                    "recurso" to recurso,
                    "longitude" to longitude.toString(),
                    "latitude" to latitude.toString(),
                    "pesquisador" to pesquisador,
                    "img1" to camera.base64_1,
                    "img2" to camera.base64_2,
                    "img3" to camera.base64_3,
                    "img4" to camera.base64_4,
                    "campo1" to campo1,
                    "campo2" to campo2,
                    "campo3" to campo3,
                    "campo4" to campo4,
                    "campo5" to campo5
            )
        }


        val imgs = activity.database?.use { select("publicacaop", "nome").whereArgs("nome={id}", "id" to nome.toString()).exec { parseSingle(StringParser) } }
        val te = imgs



        val todoCursor=  activity.database!!.writableDatabase.rawQuery("Select * from publicacaop",null)
        val old= CA?.swapCursor(todoCursor)
        old?.close()
        //dismiss() fecha o fragment */

       startActivity(Intent(this, ListviewpubpesqActivity::class.java))
       finish()


    }


    /*fun webviewget(){
        val webView =  findViewById<WebView>(R.id.webView1) as WebView
        //getWindow().requestFeature(Window.FEATURE_PROGRESS); // so se tiver o contente

        val activity = this
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show()
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://192.168.15.5/limesurvey/admin/");

    }*/

}
