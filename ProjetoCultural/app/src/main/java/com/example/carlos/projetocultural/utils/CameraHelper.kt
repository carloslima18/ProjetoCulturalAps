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
import android.widget.Toast
import com.example.carlos.projetocultural.PrincipalActivity
import org.jetbrains.anko.alert
import java.io.FileNotFoundException


//conversoes e utilidades para camera e ims/fotos
class CameraHelper {
    companion object { private val TAG = "camera" }
    var file: File?=null

    // Intent para chamar a câmera (tirar a foto)
    fun open(context: Context, fileName: String): Intent {
        file = getSdCardFile(context, fileName)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        return intent
    }

    //pegar foto já tirada (pega da galeria)
    fun openFoto(context: Context, fileName: String): Intent {
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

        val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
      //  intent.putExtra("filename",fileName)
        return intent
    }

    // Cria o arquivo da foto
    fun getSdCardFile(context: Context, fileName: String): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!dir.exists()) {
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
    fun uriForBitmap(context: Context,uri: Uri?):Bitmap{
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
        return bitmap
    }

    //Uri para base64
    fun uriForBase64(context: Context,uri: String?):String{
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
    fun onSaveInstanceState(outState: Bundle) {
        if (file != null) {
            outState.putSerializable("file", file)
        }
    }

}