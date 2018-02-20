package com.example.carlos.projetocultural

import com.google.android.gms.common.ConnectionResult
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import android.support.v7.app.AppCompatActivity



/**
 * Created by carlo on 24/01/2018.
 */
class LocationActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this) //Be aware of state of the connection
                .addOnConnectionFailedListener(this) //Be aware of failures
                .build()

        //Tentando conexão com o Google API. Se a tentativa for bem sucessidade, o método onConnected() será chamado, senão, o método onConnectionFailed() será chamado.
        googleApiClient!!.connect()

    }

    override fun onStop() {
        super.onStop()
        pararConexaoComGoogleApi()
    }

    fun pararConexaoComGoogleApi() {
        //Verificando se está conectado para então cancelar a conexão!
        if (googleApiClient!!.isConnected) {
            googleApiClient!!.disconnect()
        }
    }

    /**
     * Depois que o método connect() for chamado, esse método será chamado de forma assíncrona caso a conexão seja bem sucedida.
     *
     * @param bundle
     */
    override fun onConnected(bundle: Bundle?) {
        //Conexão com o serviços do Google Service API foi estabelecida!
    }

    /**
     * Esse método é chamado quando o client está temporariamente desconectado. Isso pode acontecer quando houve uma falha ou problema com o serviço que faça com que o client seja desligado.
     * Nesse estado, todas as requisições e listeners são cancelados.
     * Não se preocupe em tentar reestabelecer a conexão, pois isso acontecerá automaticamente.
     * As aplicações devem desabilitar recursos visuais que estejam relacionados com o uso dos serviços e habilitá-los novamente quando o método onConnected() for chamado, indicando reestabelecimento da conexão.
     */
    override fun onConnectionSuspended(i: Int) {
        // Aguardando o GoogleApiClient reestabelecer a conexão.
    }

    /**
     * Método chamado quando um erro de conexão acontece e não é possível acessar os serviços da Google Service.
     *
     * @param connectionResult
     */
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        //A conexão com o Google API falhou!
        pararConexaoComGoogleApi()
    }
}