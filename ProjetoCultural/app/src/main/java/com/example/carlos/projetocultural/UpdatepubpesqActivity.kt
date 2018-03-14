package com.example.carlos.projetocultural

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.widget.*
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import org.jetbrains.anko.alert
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import org.jetbrains.anko.db.*
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
//import android.app.Fragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.R
import com.example.carlos.projetocultural.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_listviewpubpesq.*
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.fragment_operacao.*

class UpdatepubpesqActivity : DialogFragment(), OnMapReadyCallback, AdapterView.OnItemSelectedListener  {


    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)
    //variaveis para receber os dados da principalActivity
    var nome:String= "" ;var categoria:String= "" ;var endereco:String= "" ;var redesc:String= "" ;var contato:String= "" ;var atvex:String= "" ;var email:String= ""
    var cnpj:String = "" ;var representacao:String = "" ;var recurso:String = "" ;var anoinicio:String = "" ;var campo1:String= "" ;var campo2:String= ""
    var campo3:String= "" ;var campo4:String= "" ;var campo5:String= ""
    //  var img1:String= ""
    var idx:Int = 0 ;val cursor: Cursor? = null ;var width:Int= 0 ;var height:Int= 0
    //variaveis para receber as imagens do BD para coloca-las para a edição
    var base64_1:String ?= "" ;var base64_2:String ?= "" ;var base64_3:String ?= "" ;var base64_4:String ?= "" ;val camera = CameraHelper() // variavel usada para estanciar a classe que cuida (de tirar foto entre conversões .......)
    //variavel para operação de captura de imagem
    var latitude:Double?=null ;var longitude:Double?=null ;var mapView : MapView?=null ;val mappub = MapPub() ;val categories = ArrayList<String>();
    var dataAdapter: ArrayAdapter<String>?=null ;var spinner: Spinner?=null ;var database: MyDatabaseOpenHelper?=null



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        nome = arguments.getString("nomelr")
        categoria = arguments.getString("categorialr")
        endereco = arguments.getString("enderecolr")
        redesc = arguments.getString("redesclr")
        contato = arguments.getString("contatolr")
        atvex = arguments.getString("atvexlr")

        email = arguments.getString("emaillr")
        cnpj = arguments.getString("cnpjlr")
        anoinicio = arguments.getString("anoiniciolr")
        recurso = arguments.getString("recursolr")
        representacao = arguments.getString("representacaolr")
        campo1 = arguments.getString("campo1lr")
        campo2 = arguments.getString("campo2lr")
        campo3 = arguments.getString("campo3lr")
        campo4 = arguments.getString("campo4lr")
        campo5 = arguments.getString("campo5lr")
        idx = arguments.getInt("idx")

        retainInstance

        val view = inflater!!.inflate(R.layout.fragment_operacao, container, false)

        //ativa a visibilidade do botão para o usuario adicionar um campo para obter mais informação


        getlocation()

        spinner = view?.findViewById<Spinner>(R.id.categoriaop) as Spinner
        //  var button = view?.findViewById<Button>(R.id.button)
        // Spinner click listener
        spinner?.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        categories.add("escola");categories.add("praça");categories.add("museus");categories.add("teatro");categories.add("feiras");categories.add("outros");
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

    //para o spinner (eu acho)
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

        atualizaIMG(0)

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
        if(campo1 != "null"){
            campo1llop.visibility = View.VISIBLE
            campo1op.setText(campo1)
        }
        if(campo2 != "null"){
            campo2llop.visibility = View.VISIBLE
            campo2op.setText(campo2)
        }
        if(campo3 != "null"){
            campo3llop.visibility = View.VISIBLE
            campo3op.setText(campo3)
        }
        if(campo4 != "null"){
            campo4llop.visibility = View.VISIBLE
            campo4op.setText(campo4)
        }
        if(campo5 != "null"){
            campo5llop.visibility = View.VISIBLE
            campo5op.setText(campo5)
        }
        if(cnpj != "null"){
            cnpjllop.visibility = View.VISIBLE
            etcnpjop.setText(cnpj)
        }
        if(anoinicio != "null"){
            anoiniciollop.visibility = View.VISIBLE
            etanoinicioop.setText(anoinicio)
        }
        if(recurso != "null"){
            campo5llop.visibility = View.VISIBLE
            campo5op.setText(recurso)
        }
        if(representacao != "null"){
            representacaollop.visibility = View.VISIBLE
            etrepresentacaoop.setText(representacao)
        }

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
    fun Buscaimg() {
        val data = activity as ListviewpubpesqActivity
        base64_1 = data.database?.use { select("publicacaop", "img1").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        base64_2 = data.database?.use { select("publicacaop", "img2").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        base64_3 = data.database?.use { select("publicacaop", "img3").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        base64_4 = data.database?.use { select("publicacaop", "img4").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
    }

    fun getlocation(){
        val data = activity as ListviewpubpesqActivity
        val latitudeS = data.database?.use { select("publicacaop", "latitude").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
        val longitudeS = data.database?.use { select("publicacaop", "longitude").whereArgs("_id={id}", "id" to idx).exec { parseSingle(StringParser) } }
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
                Glide.with(context).load(base64_1).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(img1op)

            }
        }
        if(base64_2 != null) {
            if (numImg == 2 || numImg == 0) {
                Glide.with(context).load(base64_2).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(img2op)
            }
        }
        if(base64_3 != null) {
            if (numImg == 3 || numImg == 0) {
               // img3op.setImageURI(null)
               // img3op.setImageURI(Uri.parse(base64_3))
                Glide.with(context).load(base64_3).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(img3op)

            }
        }
        if(base64_4 != null) {
            if (numImg == 4 || numImg == 0) {
                Glide.with(context).load(base64_4).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(img4op)

            }
        }
    }

    // vai executar a cada volta do fragment para o atv principal e vice versa
    override fun onResume() {
        super.onResume()
        mapView?.onResume();

        val width = getResources().getDisplayMetrics().widthPixels
        //val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = (getResources().getDisplayMetrics().heightPixels * 0.8)
        //val height = resources.getDimensionPixelSize(R.dimen.)
        dialog.window!!.setLayout(width, height.toInt())

        context.registerReceiver(broadcastReceive(), IntentFilter(MapPub.loc_receiver))

        editMapop.setOnClickListener(){
            getlocation()
            val intent = Intent(context, MapPub::class.java)
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

        val img1op = view?.findViewById<ImageView>(R.id.img1op) as ImageView
        val img2op = view?.findViewById<ImageView>(R.id.img2op) as ImageView
        val img3op = view?.findViewById<ImageView>(R.id.img3op) as ImageView
        val img4op = view?.findViewById<ImageView>(R.id.img4op) as ImageView

        val actt = activity as ListviewpubpesqActivity
        val fragment = this@UpdatepubpesqActivity
        //para quando clicar nas imgs chama o metodo tirafoto para caso necessite a troca(edição)
        img1op.setOnClickListener(){
            camera.tirafoto(1,actt,fragment,img1op,img2op,img3op,img4op)
        }
        img2op.setOnClickListener(){
            camera.tirafoto(2,actt,fragment,img1op,img2op,img3op,img4op)}
        img3op.setOnClickListener(){
            camera.tirafoto(3,actt,fragment,img1op,img2op,img3op,img4op)
        }
        img4op.setOnClickListener(){
            camera.tirafoto(4,actt,fragment,img1op,img2op,img3op,img4op)
        }



        //ações para os botoes delete e salvar (referente as edições) no fragment. Chamando os reespectivos metodos
        atteditop.setOnClickListener(){onClickupdate()}
        attdeleteop.setOnClickListener(){onClickdelete()}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val listviewpubpesqActivity = activity as ListviewpubpesqActivity
        camera.supportForOnActivityResult(data,resultCode,requestCode,idx,"update",listviewpubpesqActivity.database)//0 pq só vai ter que passar o valor do id, se for em caso de update/alterar o dado, e add, pq quando é no caso tbm de alteração..
        //precisa mudar o caminho na classe camera!
    }

    val androidutils = AndroidUtils
    //variavel para indicar qual foto o usuario vai editar
    fun onClickupdate(){
        val categoria:String = spinner?.selectedItem.toString()
        val listviewpubpesqActivity = activity as ListviewpubpesqActivity
        androidutils.updateTablepesq(CA,listviewpubpesqActivity.database,nomeop.text.toString(),redesocialop.text.toString(),enderecoop.text.toString(),contatoop.text.toString(),
                emailop.text.toString(),atvexop.text.toString(), categoria, campo1op.text.toString(), campo2op.text.toString(),campo3op.text.toString(),
                campo4op.text.toString(),campo5op.text.toString(),latitude.toString(),longitude.toString(),"pesquisador",anoinicio,cnpj,
                representacao,recurso,base64_1,base64_2,base64_3,base64_4,idx)

        var todoCursor: Cursor?=null

        todoCursor= listviewpubpesqActivity.database!!.writableDatabase.rawQuery("Select * from publicacaop",null) //usa o cursor para armazena a pesquisa do BD
        CA= ToDoAdapter(context,todoCursor!!,"PrincipalActivity")// pega o cursor que ta a consulta com os dados feita na linah de cima, e joga no CA(apapter para ser preenchido a listRow)
        lvPubpesq.adapter=CA// seta o adapter da listView com o CursorAdapter criado(que possui a consulta do banco de dados) e seta na listView (fazendo com que a classe toDoAdapter mapeia os dados)
        CA?.swapCursor(todoCursor)
        listviewpubpesqActivity.lvPubpesq.adapter = CA
        dismiss()

    }


    //exclui toda a publicação
    fun onClickdelete(){
         // val mainact = activity as PrincipalActivity
         context.alert("deseja apagar esta publicação") {
            title = "Ação"
            negativeButton("deletar") {
                //val ID = lvPubPrinc.getAdapter().getItemId(idx)
                val data = activity as ListviewpubpesqActivity
                data.database?.use {
                    delete("publicacaop", "_id = \"" + idx + "\"")
                }
                Toast.makeText(context,"Item Deletado!!", Toast.LENGTH_SHORT).show()
                val todoCursor = data.database!!.writableDatabase.rawQuery("Select * from publicacaop", null)
                CA?.swapCursor(todoCursor)
                data.lvPubpesq.adapter = CA
                dismiss()
            }
            positiveButton("voltar"){

            }
        }.show()
    }

    companion object {
        val KEY2 = "update_fragment"
    }
}
