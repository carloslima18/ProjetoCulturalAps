package com.example.carlos.projetocultural.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

//perifica a internet
object AndroidUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = connectivity.allNetworks
        return networks
                .map { connectivity.getNetworkInfo(it) }
                .any { it.state == NetworkInfo.State.CONNECTED };
    }
}