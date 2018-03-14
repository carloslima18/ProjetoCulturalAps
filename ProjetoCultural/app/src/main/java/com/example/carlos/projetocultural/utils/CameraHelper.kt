package com.example.carlos.projetocultural.utils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Base64
import android.util.Log
import com.example.carlos.projetocultural.domain.pubService
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.provider.MediaStore.Images
import android.content.ContentResolver
import android.database.Cursor
import android.os.PersistableBundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.Toast
import br.edu.computacaoifg.todolist.MyDatabaseOpenHelper
import br.edu.computacaoifg.todolist.ToDoAdapter
import com.example.carlos.projetocultural.HomeActivity
import com.example.carlos.projetocultural.PrincipalActivity
import com.example.carlos.projetocultural.extensions.toast
import com.example.carlos.projetocultural.FormActivity
import com.example.carlos.projetocultural.ListviewpubpesqActivity
import kotlinx.android.synthetic.main.content_principal.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_operacao.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseSingle
import org.jetbrains.anko.db.select
import java.io.FileNotFoundException

import org.jetbrains.anko.db.*


//conversoes e utilidades para camera e ims/fotos
class CameraHelper : AppCompatActivity(){
    companion object { private val TAG = "camera" }
    var file: File?=null

    var database: MyDatabaseOpenHelper?=null //para usar o BD
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        database = MyDatabaseOpenHelper.getInstance(applicationContext) //estancia o BD na variavel para uso com slqLIte
    }

    // Intent para chamar a câmera (tirar a foto)
    fun open(context: Context, fileName: String): Intent {
        file = getSdCardFile(contextx, fileName)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        return intent
    }

    //pegar foto já tirada (pega da galeria)
    fun openFoto(context: Context?, fileName: String): Intent {
        file = getSdCardFile(context, fileName)
        //Log.d("camera",file.toString())
        //PARA ABRIR A GALERIA JUNTO COM OUTROS MEIO DE IMPORTA A FOTO
        val intent = Intent(MediaStore.AUTHORITY)
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            intent.setAction(Intent.ACTION_GET_CONTENT)
        }else{
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }
        intent.setType("image/*");

        val uri = FileProvider.getUriForFile(context, context?.applicationContext?.packageName + ".provider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
      //  intent.putExtra("filename",fileName)
        return intent
    }

    // Cria o arquivo da foto
    fun getSdCardFile(context: Context?, fileName: String): File {
        val dir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (dir!!.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }

    fun getSdCardFile2(fileName: String): File {
        val dir = Environment.getExternalStorageDirectory()
        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir, fileName)
    }


    // Lê o bitmap no tamanho desejado
    fun getBitmapExternal(file: File?,w: Int, h: Int): Bitmap? {
            if (file != null) {
                //Log.d(TAG, file.absolutePath)
                // Resize
                val bitmap = ImageUtils.resize(file, w, h)
                Log.d(TAG, "getBitmap w/h: " + bitmap.getWidth() + "/" + bitmap.getHeight())

                return bitmap
            }
        return null
    }

    // Lê o bitmap no tamanho desejado
    fun getBitmap(w: Int, h: Int): Bitmap? {
        file?.apply {
            if (exists()) {
                Log.d(TAG, absolutePath)

                // Resize
                val bitmap = ImageUtils.resize(this, w, h)
                Log.d(TAG, "getBitmap w/h: " + bitmap.getWidth() + "/" + bitmap.getHeight())

                return bitmap
            }
        }
        return null
    }

    // Salva o bitmap reduzido no arquivo (para upload)
    fun save(bitmap: Bitmap) {
        file?.apply {
            val out = FileOutputStream(this)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            Log.d(TAG, "Foto salva: " + absolutePath)
        }
    }

    fun retornaBase64():String{
        file?.apply {
            val r = pubService.postFoto(file)
           return r
        }
        return ""
    }

    fun retornaBase64ForMemorExt(file:File?):String{
        val r = pubService.postFoto(file)
        return r
    }


    //----------------------------------base64 para...

    fun base64ForBitmap(base64:String?):Bitmap{
        val image:ByteArray = Base64.decode(base64?.substring(base64.indexOf(",")+1), Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        return decodedBitmap
    }

    fun base64ForBitmap2(base64:String):Bitmap{
        val byteArray = org.apache.commons.codec.binary.Base64.decodeBase64(base64.toByteArray())
        val bitmapx = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        return bitmapx
    }
    //----------------------------------uri para...

    //uri para bitmap
    fun uriForBitmap(context: Context?,uri: Uri?):Bitmap{
        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
        return bitmap
    }

    //Uri para base64
    fun uriForBase64(context: Context,uri: String?):String{
        val t = Uri.parse(uri)
        val bitmap = uriForBitmap(context,Uri.parse(uri))
        val base64 = bitmapForBase64(bitmap)
        return  base64

        /*  //desse jt n deu certo
            val bitmapImg = uriForBitmap(context,uri) //pega o bitMap atraves da URI
            val out = FileOutputStream(File(uri.toString()))
            bitmapImg.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            val base64 = bitmapForBase64(bitmapImg) //dps usa o bitmap para pegar o base64 para assim enviar usar e gravar no BD
            return base64 */
    }

    //-------------------------------bitmap para...

    //bitmap para base64
    fun bitmapForBase64(bitmap: Bitmap?): String {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    //bitmap para uri
    fun bitmapForUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun uriToByteArray(context: Context,uri: Uri): ByteArray? {
        var data: ByteArray? = null
        try {
            val cr = context.contentResolver
            val inputStream = cr.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            data = baos.toByteArray()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return data
    }

    fun convertFileinUri(context: Context): Uri? {
        file.apply {
            val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)

            return uri
        }
        return null
    }

    fun base64forUri(context: Context,base64:String):Uri{
        val bitmap = base64ForBitmap(base64)
        val uri = bitmapForUri(context,bitmap)
        return uri
    }

    // Se girou a tela recupera o estado
    fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            file = savedInstanceState.getSerializable("file") as File
        }
    }

    // Salva o estado
  /*  fun onSaveInstanceState(outState: Bundle) {
        if (file != null) {
            outState.putSerializable("file", file)
        }
    } */



























    //para tirar foto


    private val REQUEST_IMAGE_CODE = 1888; //variaveis obrigatorias para parametro da funcao "startActivityForResult"
    val PICK_FROM_FILE = 2;
    var base64_1:String?=null  //variaveis que recebe a string BASE64 das imagens para..
    var base64_2:String?=null  //..salvar no banco de dados
    var base64_3:String?=null
    var base64_4:String?=null
    var imgC1:ImageView ?= null
    var imgC2:ImageView ?= null
    var imgC3:ImageView ?= null
    var imgC4:ImageView ?= null
    var contextx:Context?=null
    var numImgx:Int?=null

    //USA PARA TIRAR A FOTO ATRAVES DA CAMERA
    fun tirafoto(numImg: Int,contextForm: Activity,fragment: DialogFragment?,img1:ImageView,img2:ImageView,img3:ImageView,img4:ImageView){
        numImgx = numImg
        contextx = contextForm
        imgC1 = img1
        imgC2 = img2
        imgC3 = img3
        imgC4 = img4
        dispatchTakePictureIntent(contextForm,fragment)             //caso tiver permissao usa metodo tira a foto
    }




    // ESTARTA A ATIVIDADE PARA TIRAR A FOTO OU ABRIR OS ARQUIVOS DE IMAGENS PARA SELECIONAR
    fun dispatchTakePictureIntent(contextForm: Activity,fragment: DialogFragment?){

        contextForm.alert("") {
            title = "Escolher fotografia"
            negativeButton("Tirar foto") {
                if(false) {
                    try {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val ms = System.currentTimeMillis()
                        val fileName = "fotopub_${ms}.jpg"
                        //  val intent = camera.open(context,fileName) // chama da classe cameraHeper as funções para tirar foto
                        val fileuri = Uri.fromFile(File(Environment.getExternalStorageDirectory(), fileName));//camera.getSdCardFile2(fileName)
                        // uricamera = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileuri)
                        startActivityForResult(intent, REQUEST_IMAGE_CODE) //estarta o intent enviando para o metodo activityResult para tratar o resultado
                    } catch (e: Exception) {
                        Toast.makeText(contextx, "erro ao tirar foto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            positiveButton("Importar") {
             //   try {
                    val ms = System.currentTimeMillis()
                    val fileName = "fotopub_${ms}.jpg"
                    var data = Intent()
                //    data.putExtra("numerodoimageview",5)
                //    setResult(Activity.RESULT_OK, data)
                // var intent = Intent(this@CameraHelper, formActivity::class.java)
                    data = openFoto(contextForm.baseContext,fileName)
                    if(fragment == null) {
                        contextForm.startActivityForResult(data, PICK_FROM_FILE)
                    }
                    else{
                        fragment.startActivityForResult(data, PICK_FROM_FILE)
                    }

             /* }catch (e:Exception){
                    Toast.makeText(act.baseContext, "Não foi possivel criar o arquivo", Toast.LENGTH_SHORT).show()
                }*/
            }
        }.show()
    }


    var CA: ToDoAdapter?= null //Adapter para preencher a listRow(cada item da ListView, referente a publicação que foi salva)  (envia os dados passados do BD, para p toDoAdapter para ser mapeados na listRow)




    fun supportForOnActivityResult(data:Intent?, resultCode: Int,requestCode:Int,idx:Int,updateoradd:String,databaseOpenHelper: MyDatabaseOpenHelper?){

        var uri :Uri?= data?.getData();
        var bitmapImg : Bitmap?=null
        if (resultCode == Activity.RESULT_OK) {//verifica se foi feito o envio com sucesso

            if(data != null && requestCode == PICK_FROM_FILE){
                uri = data.data
                val takeFlags = data.getFlags() and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION) //questão de permissão para acessa o arquivo
                contextx?.getContentResolver()?.takePersistableUriPermission(uri, takeFlags)//tbm questão de permissão para manipular os arquivos da foto
                bitmapImg = uriForBitmap(contextx,uri) //pega o bitMap atraves da URI
            }

            if(requestCode == REQUEST_IMAGE_CODE) {
                bitmapImg = getBitmap(300, 300) //converte o bit map com a largura especifica + sobre a função no arquivo CameraHelper
                if (bitmapImg != null) {
                    save(bitmapImg) //salva o bitmap no caminho absoluto
                    uri = FileProvider.getUriForFile(contextx, contextx?.applicationContext?.packageName + ".provider", file)
                }
            }

            if(uri != null) {
                when (requestCode) {
                    REQUEST_IMAGE_CODE, PICK_FROM_FILE -> { //Pode ser da camera ou da galeria

                        if(updateoradd == "update"){
                            updatefoto(numImgx,uri,bitmapImg,idx,databaseOpenHelper)
                        }
                        else{
                            addfoto(numImgx,uri,bitmapImg)
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
        }else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(contextx, "Falha na captura", Toast.LENGTH_SHORT).show()
        }
    }


    fun addfoto(numImgx:Int?,uri: Uri?,bitmapImg:Bitmap?){
        if (numImgx == 1) {
            //uri1 = data.data
            //img1.setImageBitmap(bitmap)
            base64_1 = uri.toString()
            imgC1?.setImageBitmap(bitmapImg)

        } else if (numImgx == 2) {
            base64_2 = uri.toString()
            imgC2?.setImageBitmap(bitmapImg)

        } else if (numImgx == 3) {
            base64_3 =uri.toString()
            imgC3?.setImageBitmap(bitmapImg)

        } else if (numImgx == 4) {
            base64_4 =uri.toString()
            imgC4?.setImageBitmap(bitmapImg)

        }
    }
    fun updatefoto(numImgx:Int?,uri: Uri?,bitmapImg:Bitmap?,idx:Int,activity:MyDatabaseOpenHelper?){
        if (numImgx == 1) {
            //uri1 = data.data
            //img1.setImageBitmap(bitmap)
            base64_1 =uri.toString()
            imgC1?.setImageBitmap(bitmapImg)
            onclickupdate2(1.toString(),base64_1,idx,activity)

        } else if (numImgx == 2) {
            //uri2 = data.data
            // img2.setImageBitmap(bitmap)
            base64_2 = uri.toString()
            imgC2?.setImageBitmap(bitmapImg)
            onclickupdate2(2.toString(),uri.toString(),idx,activity)

        } else if (numImgx == 3) {
            // uri3 = data.data
            //  img3.setImageBitmap(bitmap)
            base64_3 = uri.toString()
            imgC3?.setImageBitmap(bitmapImg)
            onclickupdate2(3.toString(),uri.toString(),idx,activity)

        } else if (numImgx == 4) {
            //  uri4 = data.data
            //img4.setImageBitmap(bitmap)
            base64_4 =uri.toString()
            imgC4?.setImageBitmap(bitmapImg)
            onclickupdate2(4.toString(),uri.toString(),idx,activity)

        }
    }


    fun onclickupdate2(edt:String,urix:String?,idx: Int,databaseOpenHelper: MyDatabaseOpenHelper?){
        databaseOpenHelper?.use {
            update("publicacaop", ("img" + edt) to urix
            )
                    .where("_id = \""+idx+"\"")
                    .exec()
        }
        val todoCursor= databaseOpenHelper!!.writableDatabase.rawQuery("Select * from publicacaop",null)
        CA?.swapCursor(todoCursor)
    }








    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var uri :Uri?= data?.getData();
        var bitmapImg : Bitmap ?=null
        if (resultCode == Activity.RESULT_OK) {//verifica se foi feito o envio com sucesso

            if(data != null && requestCode == PICK_FROM_FILE){
                uri = data.data
                val takeFlags = data.getFlags() and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION) //questão de permissão para acessa o arquivo
                contextx?.getContentResolver()?.takePersistableUriPermission(uri, takeFlags)//tbm questão de permissão para manipular os arquivos da foto
                bitmapImg = uriForBitmap(contextx,uri) //pega o bitMap atraves da URI
            }

            if(requestCode == REQUEST_IMAGE_CODE) {
              bitmapImg = getBitmap(300, 300) //converte o bit map com a largura especifica + sobre a função no arquivo CameraHelper
                if (bitmapImg != null) {
                    save(bitmapImg) //salva o bitmap no caminho absoluto
                    uri = FileProvider.getUriForFile(contextx, contextx?.applicationContext?.packageName + ".provider", file)
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
                            img1a?.setImageBitmap(bitmapImg)

                        } else if (numImgx == 2) {
                            base64_2 = uri.toString()
                            imgC2?.setImageBitmap(bitmapImg)

                        } else if (numImgx == 3) {
                            base64_3 =uri.toString()
                            imgC3?.setImageBitmap(bitmapImg)

                        } else if (numImgx == 4) {
                            base64_4 =uri.toString()
                            imgC4?.setImageBitmap(bitmapImg)

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
        }else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(contextx, "Falha na captura", Toast.LENGTH_SHORT).show()
        }

    }*/





}