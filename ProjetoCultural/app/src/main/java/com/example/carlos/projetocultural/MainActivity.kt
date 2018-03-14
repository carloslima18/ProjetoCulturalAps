package com.example.carlos.projetocultural

import com.example.carlos.projetocultural.extensions.fromJson
import com.example.carlos.projetocultural.utils.HttpHelper
import com.example.carlos.projetocultural.extensions.*

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.DrmInitData
import android.os.*
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import com.example.carlos.projetocultural.PubAdapter
import com.example.carlos.projetocultural.domain.GetRequisitaPub

import org.jetbrains.anko.custom.async
import org.json.JSONObject
import com.example.carlos.projetocultural.extensions.setupToolbar
import com.google.android.gms.internal.zzagr
import com.google.android.gms.internal.zzagr.runOnUiThread
import kotlinx.android.synthetic.main.activity_map_pub.*
import kotlinx.android.synthetic.main.list_row_main.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.*
import org.jetbrains.anko.browse
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.concurrent.timerTask


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var database: MyDatabaseOpenHelper?=null
    val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1 //variaveis para permissões
    val MY_WRITE_EXTERNAL_STORAGE = 1
    val MY_READ_EXTERNAL_STORAGE = 1
    val MY_MANEGER_DOCUMENT = 1
    val MY_INTERNET = 1
    val MY_ACCESS_FINE_LOCATION = 1
    var CL:Listadapter?= null

    val SPLASH_TIME = 4000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.extras
        if(extras == null){
            val actt = Intent(this@MainActivity, splashActivity::class.java)
            startActivity(actt)
        }

        setContentView(R.layout.activity_main)
        // setupToolbar(R.id.toolbar)
        setSupportActionBar(toolbar)
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
        GetPermission()
       /* if(Andro.idUtils.isNetworkAvailable(applicationContext)) {
            val listV: ListView = findViewById<ListView>(R.id.lvPubFirst) as ListView
            val getp = GetRequisitaPub(this.applicationContext,listV,"&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1")
            getp.execute()
        }else {
            toast("sem conexão")
        }*/

        vermais.visibility = View.VISIBLE
        texto2.visibility = View.INVISIBLE


        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }


    override fun onResume() {
        super.onResume()
        buttonComentario.setOnClickListener {
            browse("http://www.anapolis.go.gov.br/portal/multimidia/noticias/ver/inscriasames-para-o-programa-estapo-abertas")
        }

        vermais.setOnClickListener{
            texto2.visibility = View.VISIBLE
            texto2.text = resources.getString(R.string.noticias2)
            vermais.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    fun mapa(){
        var listItems :ArrayList<JSONObject> = arrayListOf()
        val URL = "http://192.168.15.5/cult/sendpubuser?_format=json&fields=id,nome,atvexercida,longitude,latitude"
        val dialog = ProgressDialog.show(this, "Um momento","buscando lugares",false,true)
        Thread{
            listItems = pubService.getPub(URL)
            runOnUiThread{
                val lat:MutableList<String> = mutableListOf()
                val log:MutableList<String> = mutableListOf()
                val nome:MutableList<String> = mutableListOf()
                val idpub:MutableList<String> = mutableListOf()
                val atv:MutableList<String> = mutableListOf()
                var i = 0
                while(i < listItems.size){
                    idpub.add(listItems[i].getString("id").toString())
                    nome.add(listItems[i].getString("nome").toString())
                    log.add(listItems[i].get("longitude").toString())
                    lat.add(listItems[i].get("latitude").toString())
                    atv.add(listItems[i].get("atvexercida").toString())
                    i++
                }
                if(listItems.size != 0) {
                    val intent = Intent(applicationContext, MapPub::class.java)
                    intent.putExtra("mostrar", "todosLocais")
                    intent.putExtra("idpub", idpub.toTypedArray())
                    intent.putExtra("longitude", log.toTypedArray())
                    intent.putExtra("latitude", lat.toTypedArray())
                    intent.putExtra("nome", nome.toTypedArray())
                    intent.putExtra("atvex", atv.toTypedArray())
                    intent.putExtra("mostrar", "nao")
                    dialog.dismiss()
                    startActivity(intent)
                }else{
                    dialog.dismiss()
                    toast("erro ao att")
                }
            }

        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
       // menuInflater.inflate(R.menu.main, menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

  /*  fun PreenchePubFirst(parametro:String){
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

       /* }else{
            val snack = Snackbar.make(it,"Sem conexão.",Snackbar.LENGTH_LONG)
            snack.show()
        }*/

    }*/


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.escolas -> {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("local","escolas")
                intent.putExtra("param","&PublicacaoSearch[categoria]=escola&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1" )
                startActivity(intent)
            }
            R.id.praças -> {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("local","pracas")
                intent.putExtra("param","&PublicacaoSearch[categoria]=praça&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1" )
                startActivity(intent)
            }
            R.id.museus ->{
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("local","museus")
                intent.putExtra("param","&PublicacaoSearch[categoria]=museus&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1" )
                startActivity(intent)
            }
            R.id.feiras ->{
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("local","feiras")
                intent.putExtra("param","&PublicacaoSearch[categoria]=feiras&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1")
                startActivity(intent)
            }
            R.id.suas_pub -> {
                startActivity(Intent(this, PrincipalActivity::class.java))
            }
            R.id.pub_recentes -> {
                startActivity(Intent(this, HomeActivity::class.java))
              //  PreenchePubFirst("&fields=id,nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,img1")
            }
            R.id.pesquisas -> {
                startActivity(Intent(this, NoticiasActivity::class.java))
            }
            R.id.contato -> {
                startActivity(Intent(this, FrontActivity::class.java))
            }
            R.id.pesquisador -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.mapa-> {
                if(AndroidUtils.isNetworkAvailable(applicationContext)) {
                    toast("Aguarde...")
                    mapa()
                }else{
                    //pegando o nome e coordenadas dos registrados no banco de dados
                    toast("sem conexão")
                    database = MyDatabaseOpenHelper.getInstance(applicationContext)
                    val longitude:List<String>? = database?.use { select("publicacao2", "longitude").exec {parseList(StringParser)} }
                    val latitude:List<String>?  = database?.use { select("publicacao2", "latitude").exec { parseList(StringParser) } }
                    val nome:List<String>? = database?.use { select("publicacao2", "nome").exec { parseList(StringParser) } }
                    val atvex:List<String>? = database?.use { select("publicacao2", "atvexercida").exec { parseList(StringParser) } }


                    //intent para enviar para o MAPPUB para abrir a tela do mapa
                    val intent = Intent(this@MainActivity, MapPub::class.java)
                    intent.putExtra("mostrar","todosLocais")
                    intent.putExtra("longitude",longitude?.toTypedArray())
                    intent.putExtra("latitude",latitude?.toTypedArray())
                    intent.putExtra("nome",nome?.toTypedArray())
                    intent.putExtra("atvex",atvex?.toTypedArray())
                    startActivity(intent)
                }

            }
        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }




    fun GetPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAPTURE_AUDIO_OUTPUT), MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_REQUEST_PHONE_CALL)
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

    fun toastx(){
        val layoutinflate = layoutInflater
        val layout = R.layout.toastm
        val viewgroup = findViewById<ViewGroup>(R.id.toast_layout_root)
        val view = layoutinflate.inflate(layout,viewgroup)
        val toastx = Toast(this)
        toastx.duration = Toast.LENGTH_LONG
        toastx.view = view
        toastx.show()
    }

/*


teste que deu errado, mas pode servir para algo um dia
   /* lateinit var dialog: ProgressDialog
    lateinit var lv_details: ListView
    internal lateinit var listView:ListView
    protected var pubs = listOf<ModelPub>()
    fun run(){
        lv_details = findViewById<ListView>(R.id.lvPubMain) as ListView
        try{

            doAsync {
                if(Androi.dUtils.isNetworkAvailable(applicationContext)) {
                    pubs = pubService.getPub()

                }else{
                    toast("verificando conexão")
                }

            }
        }catch (e:Exception){
            toast("verificando conexão")
        }
    }*/


    var input: InputStream? = null
    var jObect: JSONObject? = null
    var json = ""
    fun getJSONFromUrl(url: String){
        try {
            val httpClient = DefaultHttpClient()
            val httpPost = HttpPost(url)
            httpPost.setHeader("content-type","application/json")
            httpPost.entity= StringEntity(httpPost.toString(),"UTF-8")
            val httpResponse = httpClient.execute(httpPost)
            val httpEntity = httpResponse.entity
            input = httpEntity.content
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val reader = BufferedReader(InputStreamReader(input, "iso-8859-1"), 8)
            val sb = StringBuilder()
            val line: String? = null
            while ((line == reader.readLine()) != null) {
                sb.append(line!! + "\n")
            }
            input!!.close()
            json = sb.toString()
            Log.i("JRF", json)
        } catch (e: Exception) {
            Log.e("Buffer Error", "Error converting result " + e.toString())
        }
        // Transforma a String de resposta em um JSonObject
        try {
            jObect = JSONObject(json)
        } catch (e: JSONException) {
            Log.e("JSON Parser", "Error parsing data " + e.toString())
        }
    }

    fun readJSONFeed(URL:String) {
        val stringBuilder = StringBuilder();
        val httpClient = DefaultHttpClient();
        val httpGet = HttpGet(URL);
        var jsonStri:String?= ""
        try {
            val response = httpClient.execute(httpGet);
            val statusLine = response.getStatusLine();
            var statusCode = statusLine.getStatusCode();
           if (statusCode == 200) {
                var entity = response.getEntity();
                var inputStream = entity.getContent();
                var reader = BufferedReader(InputStreamReader(inputStream));
                var line:String = "" ;
                 while ({ line = reader.readLine(); line }() != null) {
                   // line = reader.readLine().toString()
                    stringBuilder.append(line + "\n")
                }
                inputStream.close();
                jsonStri = stringBuilder.toString()


            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (e: Exception) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }

    }

    var todoCursor: Cursor?=null
    fun mont(){
        database = MyDatabaseOpenHelper.getInstance(applicationContext)
        todoCursor= database!!.writableDatabase.rawQuery("Select * from driver",null)
        var CA:ToDoAdapter?= null
        CA?.swapCursor(todoCursor)
        CA?.notifyDataSetChanged()
    //---->    lvItems.invalidateViews();
        sync("http://192.168.15.111/cult/sendpubuser?_format=json")
    }
    fun sync(url:String){
        val timer = Timer()
       // timer.schedule(timerTask {
            //async {
                val A = URL(url).readText()
                var jArray = JSONArray(A)
                var tes = jArray[3]

                var E = A.replace("}]", "")
                E = E.replace("[{\"", "")
                E = E.replace("\"", "")
                val AA = E.split("},{")
        //        database = MyDatabaseOpenHelper.getInstance(applicationContext)
        //        database?.use {
        //            delete("publicacao2")
        //        }
                AA.forEach {
                    val B = it
                    val C = B.split(",")
                    val nome = C[1].replace("nome", "").replace(":", "")
                    val redesocial = C[2].replace("redesocial", "").replace(":", "")
                    val endereco = C[3].replace("endereco", "").replace(":", "")
                    val contato = C[4].replace("contato", "").replace(":", "")
                    val atvexercida = C[5].replace("atvexercida", "").replace(":", "")
                    val categoria = C[6].replace("categoria", "").replace(":", "")
                    val latitude = C[7].replace("latitude", "").replace(":", "")
                    val longitude = C[8].replace("longitude", "").replace(":", "")
                    val img1 = C[9].replace("img1", "").replace(":", "")
                    val img2 = C[10].replace("img2", "").replace(":", "")
                    val img3 = C[11].replace("img3", "").replace(":", "")
                    val img4 = C[12].replace("img4", "").replace(":", "")
                    database?.use {
                        insert("publicacao",
                                "nome" to nome,
                                "redesocial" to redesocial,
                                "endereco" to endereco,
                                "contato" to contato,
                                "atvexercida" to atvexercida,
                                "categoria" to categoria,
                                "img1" to img1,
                                "img2" to img2,
                                "img3" to img3,
                                "img4" to img3,
                                "longitude" to longitude,
                                "latitude" to latitude
                        )
                    }
                }
                mont()
         //   }
       // },3000 )
    }
*/



    /* fun getpubs(){
     Thread {
             listItems = pubService.getPub()
         runOnUiThread {
             val listV: ListView = findViewById<ListView>(R.id.lvPubMain) as ListView
             listV.adapter = Listadapter(this, R.layout.list_row_main, R.id.textViewnomeM, listItems)
         }
     }.start()


/*      val permissionCheck = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
     val listV: ListView = findViewById<ListView>(R.id.lvPubMain) as ListView
     if(Andr.oidUtils.isNetworkAvailable(applicationContext) && permissionCheck == 0) {
         Thread {
             listItems = pubService.getPub()
             runOnUiThread {
                 listV.adapter = Listadapter(this, R.layout.list_row_main, R.id.textViewnomeM, listItems)
             }
         }.start()

     }*/

 } */

}
