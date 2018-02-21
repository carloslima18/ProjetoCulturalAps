package com.example.carlos.projetocultural

import android.Manifest
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor



import android.graphics.Point
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import android.widget.Toast
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.common.ConnectionResult
import kotlinx.android.synthetic.main.activity_principal.*


import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select
import org.json.JSONObject


class PrincipalActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1 //variaveis para permissões
    val MY_WRITE_EXTERNAL_STORAGE = 1
    val MY_READ_EXTERNAL_STORAGE = 1
    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)
    var database: MyDatabaseOpenHelper?=null //para usar o BD
    var todoCursor: Cursor?=null //para guardar e manipular os dados do BD
    var permission = false  //variavel para verificar permissão
    val REQUEST_PESMISSION_GPS=1 //para GPS
    var googleApiClient : GoogleApiClient ?= null //..
    var latitude: Double = 0.0//variaveis para as coordenadas
    var longitude: Double = 0.0

    val camera =CameraHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        //setupToolbar(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
     //   supportActionBar?.setDisplayHomeAsUpEnabled(true)		//ativa o menu o botao dos 3 tracos.
     //   supportActionBar?.setDisplayShowHomeEnabled(true)

        database = MyDatabaseOpenHelper.getInstance(applicationContext) //estancia o BD na variavel para uso com slqLIte
        todoCursor= database!!.writableDatabase.rawQuery("Select * from publicacao",null) //usa o cursor para armazena a pesquisa do BD
        CA= ToDoAdapter(this,todoCursor!!)// pega o cursor que ta a consulta com os dados feita na linah de cima, e joga no CA(apapter para ser preenchido a listRow)
        lvPubPrinc.adapter=CA// seta o adapter da listView com o CursorAdapter criado(que possui a consulta do banco de dados) e seta na listView (fazendo com que a classe toDoAdapter mapeia os dados)

        //botao para abrir o fragment para inserir uma bublicação, e tbm verifica permissões necessarias para fazer essa inserção
        FAB_Principal.setOnClickListener {


                addFragment(applicationContext) //chama a função addFragment(que cuida da insersão de uma publicação)

        }

        //ativa o botao para deletar ou editar a publicação quando clica em alguma item da list view
        lvPubPrinc.setOnItemClickListener {
            adapterView, view, i, l -> operacaoFragment(view, lvPubPrinc.getAdapter().getItemId(i).toInt(),applicationContext)//lvPubMain.getAdapter().getItemId(i).toInt()
        }

        // quando clica e segura em algum item da list view, ativa esse botão para enviar os dados para o servidor
        lvPubPrinc.setOnItemLongClickListener {
            adapterView, view, i, l -> sendServer(view,lvPubPrinc.getAdapter().getItemId(i).toInt())
        }

        //Adicionando o Location Service API (do google) na variavel para ser usada (PARA PEGAR A POSIÇÃO ATUAL DO USUARIO)
        //chamando a classe, e ativando para que envie uma mensagem de broadcast, que vai ser passado a posição do cliente e pegado nessa atividad logo quando executar essa linha (vai ser pego com a função de briadcast)
        googleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()

    }

    //para estarta a função onConnected quando abrir a atv para assim pegar as coordenadas
    override fun onStart() {
        super.onStart()
        if (googleApiClient != null) {
            googleApiClient?.connect()
        }
    }

    //função para conectar no serviço do GPS para obter a posição atual do usuario(lat e long)
    override fun onConnected(p0: Bundle?) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
    public inner class broadcastReceiver: BroadcastReceiver(){
        public var newLat:String?=null
        public var newLog:String?=null
        override fun onReceive(context: Context?,intent: Intent?){
            newLat = intent?.getStringExtra("latMap")//foi passado esse intent que esta sendo recebido aq na MapPub activity
            newLog = intent?.getStringExtra("logMap")
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
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

    //chama o fragment de adicionar a publicação
    private fun addFragment(context: Context){
        val ft= supportFragmentManager.beginTransaction()
        val fragAnterior = supportFragmentManager.findFragmentByTag(AddFragment.KEY)
        if (fragAnterior != null) {
            ft.remove(fragAnterior)
        }

        // para pegar a altura e largura do dispositivo em relação a tela para estabelecer alt e larg na tela do fragment de addPublicação
        val metrics = DisplayMetrics()
        var windowmag =context.getSystemService(Context.WINDOW_SERVICE)//não sei para que eu coloquei isso aq
        windowManager.getDefaultDisplay().getMetrics(metrics);//tbm para pegar o tam da tela
        val height = metrics.heightPixels;//pega o tamanho em pixeis
        val width = metrics.widthPixels;

        val bundle = Bundle()//cria um bundle para enviar os dados necessarios para o fragment
        onConnected(bundle) // ativa a função onConnecte para atualizar os dados da localização do usuario
        bundle.putDouble("latitude",latitude)//pega os dados da localização
        bundle.putDouble("longitude",longitude)
        bundle.putInt("height", height)//pega os daddos da tela
        bundle.putInt("width",width)
        ft.addToBackStack(null)//padrao, adicionado a atv a pilha do android
        val dialog = AddFragment()//estancia o fragment que sera chamado numa variavel para passar o bundle como parametro
        dialog.arguments= bundle// passa o bundle como parametro
        dialog.show(ft, AddFragment.KEY) //chama o add fragment para ser vizualizado paara o usuario.
    }

    //chama o fragment para editar ou excluir a publicao
    private fun operacaoFragment(view: View?, idx:Int, context: Context){
        val ft= supportFragmentManager.beginTransaction()
        val fragAnterior = supportFragmentManager.findFragmentByTag(OperacaoFragment.KEY2)
        if (fragAnterior != null) {
            ft.remove(fragAnterior)
        }
        ft.addToBackStack(null)

        // para pegar a altura e largura para assim usar no dialog para setar e determina as dimensões do fragment
        val metrics = DisplayMetrics()
        var windowmag =this.getSystemService(Context.WINDOW_SERVICE)
        var display = windowManager.getDefaultDisplay()
        var size = Point()
        display.getSize(size)
        val height = size.x
        val width = size.y

        val bundle= Bundle()//bundle para passagem de parametros(dados a baixo) para outra atv
        bundle.putString("nome",(view?.findViewById<TextView>(R.id.textViewnome))?.text.toString())
        bundle.putString("categoria",(view?.findViewById<TextView>(R.id.textViewtipo))?.text.toString())
        bundle.putString("endereco",(view?.findViewById<TextView>(R.id.textViewend))?.text.toString())
        bundle.putString("redesc",(view?.findViewById<TextView>(R.id.textViewredeSoc))?.text.toString())
        bundle.putString("contato",(view?.findViewById<TextView>(R.id.textViewcontato))?.text.toString())
        bundle.putString("atvex",(view?.findViewById<TextView>(R.id.textViewAtcEx))?.text.toString())
        bundle.putInt("height", height.toInt())
        bundle.putInt("width",width.toInt())
        bundle.putInt("idx",idx)
        val dialog = OperacaoFragment()//estancia o fragment para passar os dados do bundle
        dialog.arguments= bundle
        dialog.show(ft, OperacaoFragment.KEY2)//chama o fragment
    }

    //essa função cuida de envia os dados da publicação salva no banco de dados da aplicação, para o webService
    //acionada quando clica e segura na publiação estando na listView
    fun sendServer(view: View?, idBD: Int): Boolean{
        val database: MyDatabaseOpenHelper?=null
        val longitude = this.database?.use { select("publicacao", "longitude").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser).toString() } }
        val latitude = this.database?.use { select("publicacao", "latitude").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser).toString() } }
        val imgA = this.database?.use { select("publicacao", "img1").whereArgs("_id={id}", "id" to idBD).exec { parseSingle(StringParser) } }
        val imgB = this.database?.use { select("publicacao", "img2").whereArgs("_id={id}", "id" to idBD).exec { parseSingle(StringParser) } }
        val imgC = this.database?.use { select("publicacao", "img3").whereArgs("_id={id}", "id" to idBD).exec { parseSingle(StringParser) } }
        val imgD = this.database?.use { select("publicacao", "img4").whereArgs("_id={id}", "id" to idBD).exec { parseSingle(StringParser) } }

      //  var byte : ByteArray? = camera.uriToByteArray(applicationContext,Uri.parse(imgA))


       // var te = camera.uriForBitmap(applicationContext,Uri.parse(imgA))
       // var ba = camera.bitmapForBase64(te)
      //  var test = camera.uriForBase64(applicationContext, imgA)
      //  var test2 = camera.uriForBase64(applicationContext, imgB)




        act.alert("Todos dados corretos?") {

            title = "Enviar publicação"
            positiveButton("Enviar") {
                try {
                    /*variaveis para
            // var imgA:String ?= "" ;var imgB:String ?= "" ;var imgC:String ?= "" ;var imgD:String ?= ""
            //var database: MyDatabaseOpenHelper?=null*/
                    //pega os dados atraves da listView(listRow) que estão setados nos textViews
                    val nome = (view?.findViewById<TextView>(R.id.textViewnome))?.text.toString()
                    val redesocial = (view?.findViewById<TextView>(R.id.textViewnome))?.text.toString()
                    val endereco = (view?.findViewById<TextView>(R.id.textViewend))?.text.toString()
                    val contato = (view?.findViewById<TextView>(R.id.textViewcontato))?.text.toString()
                    val atvexercida = (view?.findViewById<TextView>(R.id.textViewAtcEx))?.text.toString()
                    val categoria = (view?.findViewById<TextView>(R.id.textViewtipo))?.text.toString()
                    //pega os dados direto do banco de dados (as imagens estão recebdno a img em BASE64)

                    /* algumas testes
               val r :File = File(Uri.parse(imgA).toString())
                val r1:Uri = Uri.parse(imgA)
                val r2 :String= Uri.parse(imgA).toString()
                val r3 = Uri.parse(imgA).path.toByteArray()
                val r4 = Uri.parse(imgA).path
                val file = File(r4.toString())
                val tamfile = file.length()
                val stream = FileInputStream()
                var t = BufferedInputStream(stream) */


                    //var tes =camera.convUriinBase64(applicationContext, Uri.parse(imgA))

                    //cria umm objeto Json para colocar os dados para serem enviados


                    /*var pubSend  = ModelPub(nome,redesocial,endereco,contato,atvexercida,categoria,latitude.toString(),longitude.toString())
                    //dialog (telinha que mostra na tela enviando bublicação e um load enquando faz o envio)
                    //  val dialog = ProgressDialog.show(applicationContext, "Download", "enviando publição, aguarde...", false, true)
                    //  dialog.show()*/
                    //if(Andr.oidUtils.isNetworkAvailable(applicationContext))
                    val atcv = this@PrincipalActivity
                    EnviaDados(atcv ,applicationContext, idBD).execute(nome,redesocial,endereco,contato,atvexercida,categoria,latitude,longitude,imgA,imgB,imgC,imgD)
                    //   dialog.dismiss() //fecha a telinha de dialog que mostra o carregamento
                }catch (e:Exception){
                    toast("erro ao enviar publicação")
                }
            }
            negativeButton("Cancelar"){ // botão para caso a pessoa apertar em cancelar o envio da publicação
                toast("Envio cancelado")
            }
        }.show()
        return true
    }


    class EnviaDados(act: PrincipalActivity,context: Context, id:Int) : AsyncTask<String, String, String>() {

        val context = context
        val idBd = id
       // var dialog = ProgressDialog(context)
        val data = JSONObject();
        val act = act
        val camera =CameraHelper()
        var dialog = ProgressDialog(context.applicationContext)
        override fun onPreExecute() {
            super.onPreExecute()
            // dialog = ProgressDialog.show(context.applicationContext, "Um momento","buscando lugares",false,true)
            context.toast("enviando...")
        }

        override fun doInBackground(vararg params: String): String? {
            data.put("nome", params[0])
            data.put("redesocial", params[1])
            data.put("endereco", params[2])
            data.put("contato", params[3])
            data.put("atvexercida", params[4])
            data.put("categoria", params[5])
            data.put("latitude", params[6])
            data.put("longitude", params[7])
            val img1 =  params[8]
            val img2 =  params[9]
            val img3 =  params[10]
            val img4 =  params[11]

            if(img1 != null){
                data.put("img1", camera.uriForBase64(context, params[8]))
            }
            if(img2 != null){
                data.put("img2", camera.uriForBase64(context, params[9]))
            }
            if(img3 != null){
                data.put("img3", camera.uriForBase64(context, params[10]))
            }
            if(img4 != null){
                data.put("img4", camera.uriForBase64(context, params[11]))
            }



            pubService.save(data)
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val database: MyDatabaseOpenHelper?=null
            var todoCursor: Cursor?=null
            act.database?.use {
                delete("publicacao", "_id = \"" + idBd + "\"")
            }
            todoCursor = act.database!!.writableDatabase.rawQuery("Select * from publicacao", null)
            act.CA?.swapCursor(todoCursor)
       //     dialog.dismiss()
            context.toast("publicação enviada")
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }






    /*para uso para a função envioFragment2 (que tbm faz o envio para webService, porem não é o melhor jt
    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while ((len == inputStream.read(buffer)) && inputStream.read(buffer) != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    //para enviar publicação (jeito não viavel) mais funciona
    private fun enviofragment2(view:View, idBD:Int):Boolean{
        alert("") {
            title = "Enviar publicação"
            negativeButton("enviar") {
                if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@PrincipalActivity, arrayOf(Manifest.permission.MANAGE_DOCUMENTS), 1)
                }
                try {
                    val nome = view.findViewById<TextView>(R.id.textViewnome).text.toString()
                    val categoria = view.findViewById<TextView>(R.id.textViewnome).text.toString()
                    val endereco = view.findViewById<TextView>(R.id.textViewnome).text.toString()
                    val redesc = view.findViewById<TextView>(R.id.textViewnome).text.toString()
                    val contato = view.findViewById<TextView>(R.id.textViewnome).text.toString()
                    val atvex = view.findViewById<TextView>(R.id.textViewnome).text.toString()
                    val longitude  = database?.use { select("publicacao", "longitude").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser).toString() } }
                    val latitude =   database?.use { select("publicacao", "latitude").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser).toString() } }

                    val img1x = database?.use { select("publicacao", "img1").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser).toString() } }
                    var uri: Uri = Uri.parse(img1x)
                    var iStream = contentResolver.openInputStream(uri)
                    val img1 = getBytes(iStream)

                    val img2x = database?.use { select("publicacao", "img2").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser) } }
                    uri = Uri.parse(img2x)
                    iStream = contentResolver.openInputStream(uri)
                    val img2 = getBytes(iStream)

                    val img3x = database?.use { select("publicacao", "img3").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser) } }
                    uri = Uri.parse(img3x)
                    iStream = contentResolver.openInputStream(uri)
                    val img3 = getBytes(iStream)

                    val img4x = database?.use { select("publicacao", "img4").whereArgs("_id={idBD}", "idBD" to idBD).exec { parseSingle(StringParser) } }
                    uri = Uri.parse(img4x)
                    iStream = contentResolver.openInputStream(uri)
                    val img4 = getBytes(iStream)

                    runOnUiThread{
                        sync_pub().execute("192.168.15.111/geolocation/position?_format=json", null, nome, redesc, endereco, contato, atvex, categoria, latitude, longitude, img1.toString(), img2.toString(), img3.toString(), img4.toString())

                            toast("publicação - " + nome + " - enviada")

                    }

                }catch (e:Exception){
                    toast("falha de envio")
                }
            }
            positiveButton("voltar") {
                toast("envio cancelado")
            }
        }.show()
        return true
    }
    */



}
