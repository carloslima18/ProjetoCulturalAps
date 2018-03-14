package com.example.carlos.projetocultural


//AIzaSyBnfiLIhSwZD1JLxn4W-x5PK8ouSKYXVJI

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//import com.google.android.gms.plus.PlusOneButton
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

import org.jetbrains.anko.db.*






/**
 * A fragment with a Google +1 button.
 */
//esta classe cuida quando acontece do usuario clicar em um item do list view, aonde ficam as publicações salvas pela classe AddFragment
//esta classe edita e exclui os dados de uma publicação já salva
class OperacaoFragment : DialogFragment(), OnMapReadyCallback, AdapterView.OnItemSelectedListener  {

    //variaveis para receber os dados da principalActivity
    var nome:String= ""
    var categoria:String= ""
    var endereco:String= ""
    var redesc:String= ""
    var contato:String= ""
    var atvex:String= ""
    var email:String= ""
    var campo1:String= ""
    var campo2:String= ""
    var campo3:String= ""
    var campo4:String= ""
    var campo5:String= ""
   //  var img1:String= ""
    var idx:Int = 0
    var idBD:Int = 0
    val cursor:Cursor? = null
    var width:Int= 0
    var height:Int= 0

    //variaveis para receber as imagens do BD para coloca-las para a edição
    var base64_1:String ?= ""
    var base64_2:String ?= ""
    var base64_3:String ?= ""
    var base64_4:String ?= ""

    val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)

    //variavel para operação de captura de imagem
    val PICK_FROM_FILE = 2;
    val REQUEST_IMAGE_CODE = 1;

    var latitude:Double?=null
    var longitude:Double?=null

    var mapView : MapView?=null

    val mappub = MapPub()
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    //create dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
    val categories = ArrayList<String>();
    var dataAdapter:ArrayAdapter<String>?=null
    var spinner:Spinner?=null
    //createView! infla a view do dialogFragment para o usuario no app
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //recebe os dados da principalActivity atrevez do bundle
        nome = arguments.getString("nomelr")
        categoria = arguments.getString("categorialr")
        endereco = arguments.getString("enderecolr")
        redesc = arguments.getString("redesclr")
        contato = arguments.getString("contatolr")
        atvex = arguments.getString("atvexlr")

        email = arguments.getString("emaillr")


        campo1 = arguments.getString("campo1lr")
        campo2 = arguments.getString("campo2lr")
        campo3 = arguments.getString("campo3lr")
        campo4 = arguments.getString("campo4lr")
        campo5 = arguments.getString("campo5lr")

        idx = arguments.getInt("idx")

        retainInstance

        val view = inflater!!.inflate(R.layout.fragment_operacao, container, false)

        getlocation()



        // Spinner element
        spinner = view?.findViewById<Spinner>(R.id.categoriaop) as Spinner
        //  var button = view?.findViewById<Button>(R.id.button)
        // Spinner click listener
        spinner?.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        categories.add("escola");
        categories.add("praça");
        categories.add("museus");
        categories.add("teatro");
        categories.add("feiras");
        categories.add("outros");
        // Creating adapter for spinner
        dataAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categories);
        dataAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.setAdapter(dataAdapter);


        //ativa o mapView do Fragment
        mapView = view?.findViewById<MapView>(R.id.figMapAddop) as MapView
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        return view
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
        mapView?.onSaveInstanceState(outState)
    }

    //fara quando estarta o fragment
    override fun onStart() {
        super.onStart()
        mapView?.onStart();
        //para mexer na altura do layout
        val height = arguments.getInt("height")
        val width = arguments.getInt("width")
       // dialog.window.setLayout(width,height)

    }
    override fun onStop() {
        super.onStop()
        try {
            context.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        mapView?.onStop();
    }
    override fun onPause() {
        try {
            context.unregisterReceiver(broadcastReceive())
        }catch (e: Exception){

        }
        super.onPause()


        mapView?.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    /*fun atualizaCoordenadasNoMapView(googleMap: GoogleMap?){
        val latlng = LatLng(latitude!!.toDouble(),longitude!!.toDouble())
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13f));
        googleMap?.addMarker(MarkerOptions().position(latlng).title("newpub").snippet("informaçao adicional").flat(true)
                .rotation(245f))
    }*/

    override fun onMapReady(googleMap: GoogleMap?) {
        if(latitude != null) {
            mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)
            googleMap?.setOnMapClickListener() {
                mappub.atualizaCoordenadasNoMapView(googleMap,latitude,longitude,nome,atvex)
            }
        }
    }

    //para capturar as imagens do banco de dados e coloca-las nas variaveis globais para serem usadas
    var database: MyDatabaseOpenHelper?=null
    fun Buscaimg() {
        val mainact = activity as PrincipalActivity
        base64_1 = mainact.database?.use { select("publicacao2", "img1").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        base64_2 = mainact.database?.use { select("publicacao2", "img2").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        base64_3 = mainact.database?.use { select("publicacao2", "img3").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        base64_4 = mainact.database?.use { select("publicacao2", "img4").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
    }


    fun getlocation(){
        val mainact = activity as PrincipalActivity
        val latitudeS = mainact.database?.use { select("publicacao2", "latitude").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        val longitudeS = mainact.database?.use { select("publicacao2", "longitude").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        latitude = latitudeS?.toDouble()
        longitude = longitudeS?.toDouble()
    }

    //observação (EU NAO FECHE O BROADCAST NO ONPAUSE)
    inner class broadcastReceive: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?){
            latitude = intent?.getStringExtra("latMap")?.toDouble()
            longitude = intent?.getStringExtra("logMap")?.toDouble()
//            context?.unregisterReceiver(broadcastReceive())

        }
    }

    //atializa as imageView do fragment
    fun atualizaIMG(numImg: Int){
        Buscaimg()
        if(base64_1 != null) {
            if (numImg == 1 || numImg == 0) {
                img1op.setImageURI(null)
                img1op.setImageURI(Uri.parse(base64_1))
               // editimg1.setImageBitmap(camera.base64ToBitmap(base64_1))
            }
        }
        if(base64_2 != null) {
            if (numImg == 2 || numImg == 0) {
                img2op.setImageURI(null)
                img2op.setImageURI(Uri.parse(base64_2))
                //editimg2.setImageURI(Uri.parse(imgB))
            }
        }
        if(base64_3 != null) {
            if (numImg == 3 || numImg == 0) {
                img3op.setImageURI(null)
                img3op.setImageURI(Uri.parse(base64_3))
            }
        }
        if(base64_4 != null) {
            if (numImg == 4 || numImg == 0) {
                img4op.setImageURI(null)
                img4op.setImageURI(Uri.parse(base64_4))
            }
        }
    }


    // vai executar a cada volta do fragment para o atv principal e vice versa
    override fun onResume() {
        super.onResume()
        //seta os nomes nos editText do fragment para sujeito a edição
        var spinnerPostion :Int?= 0
        spinnerPostion = 0
        if (!categoria.equals(null)) {
            spinnerPostion = dataAdapter!!.getPosition(categoria)
        }
        nomeop.setText(nome)
        categoriaop.setSelection(spinnerPostion)
        enderecoop.setText(endereco)
        redesocialop.setText(redesc)
        contatoop.setText(contato)
        atvexop.setText(atvex)
        emailop.setText(email)
        campo1op.setText(campo1)
        campo2op.setText(campo2)
        campo3op.setText(campo3)
        campo4op.setText(campo4)
        campo5op.setText(campo5)

        mapView?.onResume();

        context.registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))

        editMapop.setOnClickListener(){
            getlocation()
            val contexto: Activity
            contexto = activity
            val intent = Intent(contexto, MapPub::class.java)
            //val locations:MutableList<String> = mutableListOf(longitude.toString(),latitude.toString())
            val longitudex:List<String> ?= mutableListOf(longitude.toString())
            val latitudex:List<String> ?= mutableListOf(latitude.toString())
            val nomex:List<String> ?= mutableListOf(nome)
            val atvex:List<String> ?= mutableListOf(atvex)
            intent.putExtra("mostrar","atualiza")
            intent.putExtra("longitude",longitudex?.toTypedArray())
            intent.putExtra("latitude",latitudex?.toTypedArray())
            intent.putExtra("nome",nomex?.toTypedArray())
            intent.putExtra("atvex",nomex?.toTypedArray())
            startActivity(intent)
        }

        //atualiza as imagens no fragment para edição

         atualizaIMG(0)


        //para quando clicar nas imgs chama o metodo tirafoto para caso necessite a troca(edição)
        img1op.setOnClickListener(){tirafoto(1)}
        img2op.setOnClickListener(){tirafoto(2)}
        img3op.setOnClickListener(){tirafoto(3)}
        img4op.setOnClickListener(){tirafoto(4)}

        //ações para os botoes delete e salvar (referente as edições) no fragment. Chamando os reespectivos metodos
        atteditop.setOnClickListener(){onClickupdate()}
        attdeleteop.setOnClickListener(){onClickdelete()}
    }

    //variavel para indicar qual foto o usuario vai editar
    var numImgx:Int = 0

    //seleciona uma foto dos arquivos (albuns do celular)
    fun tirafoto(numImg:Int){
        numImgx = numImg
        dispatchTakePictureIntent()             //caso tiver permissao usa metodo tira a foto
    }

    // ESTARTA A ATIVIDADE PARA TIRAR A FOTO OU ABRIR OS ARQUIVOS DE IMAGENS PARA SELECIONAR
    fun dispatchTakePictureIntent(){
        val act = activity as PrincipalActivity
        act.alert("") {
            title = "Escolher fotografia"
            negativeButton("Tirar foto") {
                if(false) {
                    try {
                        val ms = System.currentTimeMillis()
                        val fileName = "fotopub_${ms}.jpg"
                        val intent = camera.open(context, fileName) // chama da classe cameraHeper as funções para tirar foto
                        startActivityForResult(intent, REQUEST_IMAGE_CODE) //estarta o intent enviando para o metodo activityResult para tratar o resultado

                    } catch (e: Exception) {
                        Toast.makeText(context, "could not create file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            positiveButton("Importar") {
                try {

                    val ms = System.currentTimeMillis()
                    val fileName = "fotopub_${ms}.jpg"
                    val intent = camera.openFoto(context,fileName)
                    startActivityForResult(intent,PICK_FROM_FILE)

                }catch (e:Exception){
                    Toast.makeText(context, "could not create file", Toast.LENGTH_SHORT).show()
                }
            }
        }.show()

    }

    fun onclickupdate2(edt:String,urix:String?){
        val mainact = activity as PrincipalActivity
        val ID = mainact.lvPubPrinc.getAdapter().getItemId(idx)
        mainact.database?.use {
            update("publicacao2", ("img" + edt) to urix
            )
                    .where("_id = \""+idx+"\"")
                    .exec()
        }
        val todoCursor=  mainact.database!!.writableDatabase.rawQuery("Select * from publicacao2",null)
        mainact.CA?.swapCursor(todoCursor)
    }

    //https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //referencia: https://stackoverflow.com/questions/22178041/getting-permission-denial-exception
    //ARMAZENA SE TIVER TIRADO OU SELECIONADO EM UM IMAGE VIEW, metodo que trata o resultado da camera ou seleção de imagem
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri = data?.getData() // pega a uri,
        if (resultCode == Activity.RESULT_OK) {//verifica se foi feito o envio com sucesso
            val bitmapImg : Bitmap ?
            if(data != null && requestCode == PICK_FROM_FILE){ //quando vc usa a importação da imagem através da galeria, a data n vem null (diferente quando se tira a foto)
                uri = data.data// pega a uri
                val takeFlags = data.getFlags() and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION) //questão de permissão para acessa o arquivo
                activity.getContentResolver().takePersistableUriPermission(uri, takeFlags)//tbm questão de permissão para manipular os arquivos da foto
                bitmapImg = camera.uriForBitmap(context,uri) //pega o bitMap atraves da URI
            }
            else{// cai no else, se a foto foi tirada a partir da camera, já que se for, a data, vem vazia(null)
                bitmapImg = camera.getBitmap(200,200) //converte o bit map com a largura especifica + sobre a função no arquivo CameraHelper
                if(bitmapImg != null){
                    camera.save(bitmapImg) //salva o bitmap no caminho absoluto
                    val file = camera.file?.absolutePath
                    val bit:Bitmap = BitmapFactory.decodeFile(file)
                    uri = camera.bitmapForUri(context,bit)
                }
            }
            /*retorna o BASE64 em string
            //  var file3 = File(data?.data.toString())
           // var stream = FileInputStream(file3)
           // var r = stream.read()
           //  var tet = pubService.postFoto(file3)

            val image:ByteArray = Base64.decode(tet,Base64.DEFAULT)
           // val but = BitmapFactory.decodeByteArray(image,0,image.size)
           // val decodedBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            img1.setImageBitmap(camera.base64ToBitmap(imgBase64))
            img2.setImageBitmap(camera.base64ToBitmap(imgBase64))
            img3.setImageBitmap(camera.base64ToBitmap(imgBase64))
            img4.setImageBitmap(camera.base64ToBitmap(imgBase64))*/

            //verifica
            if(true) {
                when (requestCode) {
                    REQUEST_IMAGE_CODE, PICK_FROM_FILE -> { //Pode ser da camera ou da galeria
                        // if (img1.drawable == null) {
                        if (numImgx == 1) {
                            //uri1 = data.data
                            //img1.setImageBitmap(bitmap)
                            base64_1 =uri.toString()
                            img1op.setImageBitmap(bitmapImg)
                            onclickupdate2(1.toString(),uri.toString())

                        } else if (numImgx == 2) {
                            //uri2 = data.data
                            // img2.setImageBitmap(bitmap)
                            base64_2 = uri.toString()
                            img2op.setImageBitmap(bitmapImg)
                            onclickupdate2(2.toString(),uri.toString())

                        } else if (numImgx == 3) {
                            // uri3 = data.data
                            //  img3.setImageBitmap(bitmap)
                            base64_3 = uri.toString()
                            img3op.setImageBitmap(bitmapImg)
                            onclickupdate2(3.toString(),uri.toString())

                        } else if (numImgx == 4) {
                            //  uri4 = data.data
                            //img4.setImageBitmap(bitmap)
                            base64_4 =uri.toString()
                            img4op.setImageBitmap(bitmapImg)
                            onclickupdate2(4.toString(),uri.toString())

                        }
                    }
                    else -> {
                        Toast.makeText(context, "requestCode não reconhecido", Toast.LENGTH_SHORT).show()
                    }
                }
            }else {
                Toast.makeText(context, "ImageBase64 ou bitmap não reconhecida", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, "ResultCode não reconhecido", Toast.LENGTH_SHORT).show()
        }

    }

    //realiza o salvamento de todos os dados editados pelo usuario no fragment
    fun onClickupdate(){
        val mainact = activity as PrincipalActivity
        val ID = mainact.lvPubPrinc.getAdapter().getItemId(idx)

        val categoria:String = spinner?.selectedItem.toString()
        mainact.database?.use {
            update("publicacao2",
                    "nome" to nomeop.text.toString(),
                    "redesocial" to redesocialop.text.toString(),
                    "endereco" to enderecoop.text.toString(),
                    "contato" to contatoop.text.toString(),
                    "email" to emailop.text.toString(),
                    "atvexercida" to atvexop.text.toString(),
                    "categoria" to categoria,
                    "campo1" to campo1op.text.toString(),
                    "campo2" to campo2op.text.toString(),
                    "campo3" to campo3op.text.toString(),
                    "campo4" to campo4op.text.toString(),
                    "campo5" to campo5op.text.toString(),
                    "img1" to base64_1,
                    "img2" to base64_2,
                    "img3" to base64_3,
                    "img4" to base64_4,
                    "latitude" to latitude,
                    "longitude" to longitude
                    )
                    .where("_id = \""+idx+"\"")
                    .exec()
        }
        val todoCursor=  mainact.database!!.writableDatabase.rawQuery("Select * from publicacao2",null)
        mainact.CA?.swapCursor(todoCursor)
        dismiss() //fecha o fragment
    }

    //exclui toda a publicação
    fun onClickdelete(){
        val mainact = activity as PrincipalActivity
        mainact.alert("deseja apagar esta publicação") {
            title = "Ação"
            negativeButton("deletar") {
                val ID = mainact.lvPubPrinc.getAdapter().getItemId(idx)
                mainact.database?.use {
                    delete("publicacao2", "_id = \"" + idx + "\"")
                }
                Toast.makeText(context,"Item Deletado!!", Toast.LENGTH_SHORT).show()
                val todoCursor = mainact.database!!.writableDatabase.rawQuery("Select * from publicacao2", null)
                mainact.CA?.swapCursor(todoCursor)
                dismiss()
            }
            positiveButton("voltar"){
                dismiss()
            }
        }.show()
    }

    //variavel abstrata para identificar o fragemnt na atv principalActivity
    companion object {
        val KEY2 = "operacao_fragment"
    }

}
