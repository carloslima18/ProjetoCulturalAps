package com.example.carlos.projetocultural.domain

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.view.WindowManager
import android.widget.ListView
import com.example.carlos.projetocultural.Listadapter
import com.example.carlos.projetocultural.MainActivity
import com.example.carlos.projetocultural.R
import org.jetbrains.anko.toast
import org.json.JSONObject

/**
 * Created by carlo on 16/02/2018.
 */
class GetRequisitaPub(context: Context, listV: ListView, param:String) : AsyncTask<Unit, Unit, String>() {

    val lv = listV
    var context = context
    var param = param//192.168.137.113
    val URL = "http://192.168.43.14/geolocation/position?_format=json$param"

    override fun onPreExecute(){
        super.onPreExecute()
        context.toast("aguarde..")
    }
    var listItems : ArrayList<JSONObject> = arrayListOf()
    override fun doInBackground(vararg params: Unit?): String? {
        listItems = pubService.getPub(URL)
        return null
    }
    override fun onPostExecute(result: String?):Unit {
        super.onPostExecute(result)
        if(listItems.size != 0) {
            lv.adapter = Listadapter(context, R.layout.list_row_main, R.id.textViewnomeM, listItems)
            context.toast("Concluido")
        }
        else{
            context.toast("nenhum item dísponivel!")
        }

    }
}