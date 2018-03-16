package com.example.carlos.projetocultural

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.carlos.projetocultural.R.id.lvPubFirst
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONException
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.carlos.projetocultural.utils.CameraHelper
import com.google.android.gms.internal.zzagr.runOnUiThread
import com.google.android.gms.maps.MapView
import org.json.JSONObject


// para pegar os dados capturados do webService e jogar na listView main, (chamada no activitiMain)
class Listadapter(internal var context: Context, internal var vg: Int, id: Int, internal var list: ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context, vg, id, list){

    var camera = CameraHelper()


   /* fun changeLista(lista: List<Noticia>) {
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }
 */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView = inflater.inflate(vg, parent, false)

        val idpub= itemView.findViewById<TextView>(R.id.idpub) as TextView
        val txtNome= itemView.findViewById<TextView>(R.id.textViewnomeM) as TextView
        val txtredesc = itemView.findViewById<TextView>(R.id.textViewredeSocM) as TextView
        val txtEnd = itemView.findViewById<TextView>(R.id.textViewendM) as TextView
        val txtCont = itemView.findViewById<TextView>(R.id.textViewcontatoM) as TextView
        val txtAtve = itemView.findViewById<TextView>(R.id.textViewAtcExM) as TextView
        val txtTipo = itemView.findViewById<TextView>(R.id.textViewtipoM) as TextView
        val txtLat = itemView.findViewById<TextView>(R.id.textViewLat) as TextView
        val txtLog = itemView.findViewById<TextView>(R.id.textViewLog) as TextView

        val base64_1 = itemView.findViewById<ImageView>(R.id.imglistrow1M) as ImageView
       /* val base64_2 = itemView.findViewById<ImageView>(R.id.imglistrow2M) as ImageView
        val base64_3 = itemView.findViewById<ImageView>(R.id.imglistrow3M) as ImageView
        val base64_4 = itemView.findViewById<ImageView>(R.id.imglistrow4M) as ImageView*/

        try {
            idpub.text = list[position].getString("id")
            txtNome.text = list[position].getString("nome")
            txtredesc.text = list[position].getString("redesocial")
            txtEnd.text = list[position].getString("endereco")
            txtCont.text = list[position].getString("contato")
            txtAtve.text = list[position].getString("atvexercida")
            txtTipo.text = list[position].getString("categoria")
            txtLat.text = list[position].getString("latitude")
            txtLog.text = list[position].getString("longitude")

            val bitmapa = list[position].getString("img1")
            //val bitmapb = list[position].getString("img2")
         /*   val bitmapc = list[position].getString("img3")
            val bitmapd = list[position].getString("img4")*/



           // val byteArray:ByteArray = org.apache.commons.codec.binary.Base64.decodeBase64(bitmapa.toByteArray())
         //   val bitmapx:Bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)


             //val uri1 = camera.base64forUri(context,bitmapa)         //ATENCAOOOO =====> =======> AQUI Q ESTA SALVANDO AS FOTOS NO APARELHO



            if (bitmapa != "null") {
                base64_1.setImageBitmap(camera.base64ForBitmap2(bitmapa))
                //Glide.with(context).load().asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(base64_1)
            }

           /* if (bitmapb != "null") {
                Glide.with(context).load(uri2).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(base64_2)
            }
            if (bitmapc != "null") {
                Glide.with(context).load(uri3).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(base64_3)
            }
            if (bitmapd != "null") {
                Glide.with(context).load(uri4).asBitmap().override(50, 50).diskCacheStrategy(DiskCacheStrategy.ALL).into(base64_4)
            }*/


        } catch (e: JSONException) {
            e.printStackTrace()

        }

        return itemView

    }


}
