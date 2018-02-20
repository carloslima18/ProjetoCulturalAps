package com.example.carlos.projetocultural
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.app.Fragment
import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.DialogFragment
//import kotlinx.android.synthetic.main.content_add.*
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.net.Uri
import android.os.Environment
import org.jetbrains.anko.alert
import java.io.File
import com.esafirm.imagepicker.features.camera.CameraModule;
import com.esafirm.imagepicker.model.Image;
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.db.*
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment.getExternalStoragePublicDirectory
import android.preference.PreferenceManager
import android.provider.ContactsContract
import android.provider.DocumentsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getExternalFilesDirs
import android.support.v4.content.ContextCompat.getNoBackupFilesDir
import android.support.v4.content.FileProvider
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import com.bumptech.glide.load.engine.Resource
import com.example.carlos.projetocultural.domain.pubService
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.custom.async
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import org.jetbrains.anko.toast
import java.io.FileInputStream
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import android.util.Base64
import android.widget.*
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.bumptech.glide.Glide
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.ImageUtils
import com.google.android.gms.internal.zzagr.runOnUiThread
import java.net.URI

//import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;

// e talvez você precisa em alguns ocations
/**
 * A fragment with a Google +1 button.
 */
// fragment para adicionar uma publicação
class AddFragment() : DialogFragment(), OnMapReadyCallback , AdapterView.OnItemSelectedListener {

    val MY_PERMISSIONS_REQUEST = 3 // para permissões
    var base64_1:String?=null  //variaveis que recebe a string BASE64 das imagens para..
    var base64_2:String?=null  //..salvar no banco de dados
    var base64_3:String?=null
    var base64_4:String?=null
    private val REQUEST_IMAGE_CODE = 1888; //variaveis obrigatorias para parametro da funcao "startActivityForResult"
    val PICK_FROM_FILE = 2;     //para tirar foto, ou importa da galeria
    var latitude:Double?= null // variaveis para pegar via intentPut extra a long e lat enviadas..
    var longitude: Double?= null //..pela PrincipalActivity
    var mapView : MapView?=null //variavel para amostragem do mapa no neste fragment
    val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)
    var numImgx:Int= 0 //variavel para identificar qual imageView que o usuario clicou la no layout
    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)

    val mappub = MapPub()
    //create dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    //infla a view! joga na tela o fragment para o usuario
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        retainInstance//para salvar a instancia do fragment caso a tela seja virada
        val view = inflater!!.inflate(R.layout.fragment_add, container, false)









        // Spinner element
       val spinner = view?.findViewById<Spinner>(R.id.spinner) as Spinner
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
        val dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(dataAdapter);


        //recebe os dados via bundle da PrincipalActivity
        latitude = arguments.getDouble("latitude")
        longitude = arguments.getDouble("longitude")

        //recebe a instancia do "obj" mapView que se encontra no layout desse fragment
        mapView = view.findViewById<MapView>(R.id.figMapAdd) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)



        //retorna a view com os dados adquiridos
        return view
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
         // On selecting a spinner item
        val item = parent?.getItemAtPosition(position).toString();
        // Showing selected spinner item
        Toast.makeText(parent?.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        try {
            context.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
    }

    override fun onResume() {
        super.onResume()
        //para quando clicar na foto, abrir a opção de tirar foto (do metodo tiraFoto) (os n 1,2,3,4 são referentes a cada imageView da fragment)
        img1.setOnClickListener(){tirafoto(1)}

        img2.setOnClickListener(){tirafoto(2)}

        img3.setOnClickListener(){tirafoto(3)}

        img4.setOnClickListener(){tirafoto(4)}

        /*fica esperando o brosCast quando a função que atualiza a coordenada no mapView na fragment e usada, e retorna
        //as novas coordenadas que o usuario selecionou no mapa*/
        context.registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))

        //padrao
        mapView?.onResume();


        /*vizualizar localização da publicação no mapa, e da a opção de vc adicionar no mapa outra coordenada caso o usuario n esteja no local
        //.. da publicacao, quando for adicionar a mesma*/
        buttonAddMap.setOnClickListener(){
            //envia a localização da onde a pessoa esta para o MapPub e da a opção caso ela queira mudar o marcador
            val contexto: Activity
            contexto = activity
            val intent = Intent(contexto, MapPub::class.java)
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

        buttonSalvar.setOnClickListener(){onClickCreate()} // para salvar a publicação, adicionando os dados no banco de dados
        // Refresh the state of the +1 button each time the activity receives focus.
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

    //OBSERVAÇÃO::::::::::::;; verificar o unreceiver no onPause
    //atualiza as variaveis com as coordenadas novas, selecionadas pelo usuario la no atv MapPub
    inner class broadcastReceive:BroadcastReceiver(){
        override fun onReceive(context: Context?,intent: Intent?){
            latitude = intent?.getStringExtra("latMap")?.toDouble()
            longitude = intent?.getStringExtra("logMap")?.toDouble()
        }
    }

    //CHAMA A FUNCAO CASO O DISPOSITIVO NÃO TENHA PERMISSAO
    //USA PARA TIRAR A FOTO ATRAVES DA CAMERA
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) { //permissao para tirar a foto
        when(requestCode){
            MY_PERMISSIONS_REQUEST -> {
                try {
                    if (grantResults.count() > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent()                                         //usa metodo tira a foto
                    }
                }catch (e:Exception){
                    Toast.makeText(context, "permissão para foto negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //USA PARA TIRAR A FOTO ATRAVES DA CAMERA
    fun tirafoto(numImg: Int){
        numImgx = numImg
        dispatchTakePictureIntent()             //caso tiver permissao usa metodo tira a foto
    }


    // ESTARTA A ATIVIDADE PARA TIRAR A FOTO OU ABRIR OS ARQUIVOS DE IMAGENS PARA SELECIONAR
    fun dispatchTakePictureIntent(){
        val act = activity as PrincipalActivity
        act.alert("") {
            title = "Escolher fotografia"
            negativeButton("Tirar foto") {
                try {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val ms = System.currentTimeMillis()
                     val fileName = "fotopub_${ms}.jpg"
                    //  val intent = camera.open(context,fileName) // chama da classe cameraHeper as funções para tirar foto
                    val fileuri  = Uri.fromFile(File(Environment.getExternalStorageDirectory(), fileName));//camera.getSdCardFile2(fileName)
                   // uricamera = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileuri)
                    startActivityForResult(intent,REQUEST_IMAGE_CODE) //estarta o intent enviando para o metodo activityResult para tratar o resultado
                }catch (e:Exception){
                    Toast.makeText(context, "possivel criar o arquivo", Toast.LENGTH_SHORT).show()
                }
            }
            positiveButton("Importar") {
                try {

                    val ms = System.currentTimeMillis()
                    val fileName = "fotopub_${ms}.jpg"
                    val intent = camera.openFoto(context,fileName)
                    startActivityForResult(intent,PICK_FROM_FILE)

                }catch (e:Exception){
                    Toast.makeText(context, "Não foi possivel criar o arquivo", Toast.LENGTH_SHORT).show()
                }
            }
        }.show()
    }


    //referencia foi para salvar a imagem e fica salvo na galeria
    //https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //referencia: https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //ARMAZENA SE TIVER TIRADO OU SELECIONADO EM UM IMAGE VIEW, metodo que trata o resultado da camera ou seleção de imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var uri :Uri?= data?.getData();
        var bitmapImg : Bitmap ?=null
        if (resultCode == RESULT_OK ) {//verifica se foi feito o envio com sucesso


            if(data != null && requestCode == PICK_FROM_FILE){ //quando vc usa a importação da imagem através da galeria, a data n vem null (diferente quando se tira a foto)
                uri = data.data// pega a uri
                /* val path1 = uri.path
                var path2 = uri.toString()
                val file = File(Environment.getExternalStorageDirectory(), uri.path)
                val file2 = File(Environment.getExternalStorageDirectory(), uri.toString())
                val path3 :Uri= Uri.fromFile(file)
                val path5 = path3.toString().replace("file://","")
                val path6 :Uri= Uri.fromFile(file2)
                var path7 = File(path5)
                val path8 = camera.file
                val yt = data.getStringExtra("filename")
                var t = getRealPathFromURI2(path3) */
                /*verifica permissão
                val permissionCheck = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_CONTACTS)*/
                val takeFlags = data.getFlags() and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION) //questão de permissão para acessa o arquivo
                activity.getContentResolver().takePersistableUriPermission(uri, takeFlags)//tbm questão de permissão para manipular os arquivos da foto
                bitmapImg = camera.uriForBitmap(context,uri) //pega o bitMap atraves da URI
             }


            if(requestCode == REQUEST_IMAGE_CODE) {
               // val dat = data?.data
               // val bundle: Bundle? = data?.extras
               // val bmp = bundle?.get("data")
                bitmapImg = camera.getBitmap(300, 300) //converte o bit map com a largura especifica + sobre a função no arquivo CameraHelper
                if (bitmapImg != null) {
                    camera.save(bitmapImg) //salva o bitmap no caminho absoluto
                     uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", camera.file)
                }
            }


            if(uri != null) {
                when (requestCode) {
                    REQUEST_IMAGE_CODE, PICK_FROM_FILE -> { //Pode ser da camera ou da galeria
                        // if (img1.drawable == null) {
                        if (numImgx == 1) {
                            //uri1 = data.data
                            //img1.setImageBitmap(bitmap)
                            base64_1 = uri.toString()
                            img1.setImageBitmap(bitmapImg)

                        } else if (numImgx == 2) {
                            base64_2 = uri.toString()
                            img2.setImageBitmap(bitmapImg)

                        } else if (numImgx == 3) {
                            base64_3 =uri.toString()
                            img3.setImageBitmap(bitmapImg)

                        } else if (numImgx == 4) {
                            base64_4 =uri.toString()
                            img4.setImageBitmap(bitmapImg)

                        }
                    }
                    else -> {
                        toast("requestCode não reconhecido")
                        //Toast.makeText(context, "requestCode não reconhecido", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                toast("Falha na uri")
                //Toast.makeText(context, "Falha na uri", Toast.LENGTH_SHORT).show()
            }


        }else if(resultCode == RESULT_CANCELED){
            Toast.makeText(context, "Falha na captura", Toast.LENGTH_SHORT).show()
        }

    }


    // quando clica no salvar, para salvar os dados da publicação
    //insere os dados da publicação no banco de dados
     fun onClickCreate(){
        val nome = editTextnome.text.toString()
        //val categoria = editText7categoria.text.toString()
        val categoria = spinner.selectedItem.toString()
        val endereco = editText4end.text.toString()
        val contato = editText5contato.text.toString()
        val redesocial = editText3redesocial.text.toString()
        val atvexercida = editText6atvEx.text.toString()
        val PrincpalActivity= activity as PrincipalActivity
         PrincpalActivity.database?.use{
            insert("publicacao",
                    "nome" to nome,
                    "redesocial" to redesocial,
                    "endereco" to endereco,
                    "contato" to contato,
                    "atvexercida" to atvexercida,
                    "categoria" to categoria,
                    "img1" to base64_1,
                    "img2" to base64_2,
                    "img3" to base64_3,
                    "img4" to base64_4,
                    "longitude" to longitude,
                    "latitude" to latitude
            )
        }

        // PrincpalActivity.CA?.notifyDataSetChanged()
         val mainact = activity as PrincipalActivity
       /* teste para ver se a imagem foi salva corretamente
         val imgs = mainact.database?.use {
             select("publicacao", "img1")
                     .whereArgs("nome={id}", "id" to nome).exec {
                 parseSingle(StringParser)
             }
         }*/
         val todoCursor=  PrincpalActivity.database!!.writableDatabase.rawQuery("Select * from publicacao",null)
         val old= PrincpalActivity.CA?.swapCursor(todoCursor)
         old?.close()
         dismiss()
    }

    companion object {
        val KEY = "add_fragment"
    }
}
