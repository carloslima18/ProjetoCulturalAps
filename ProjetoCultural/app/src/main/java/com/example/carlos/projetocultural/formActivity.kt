package com.example.carlos.projetocultural

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.widget.Toast
import android.webkit.WebViewClient





class formActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
      //  val webview =  WebView(this);
      //  setContentView(webview);
        webviewget()

    }


    fun webviewget(){
        val webView =  findViewById<WebView>(R.id.webView1) as WebView
        //getWindow().requestFeature(Window.FEATURE_PROGRESS); // so se tiver o contente

        val activity = this
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show()
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://www.google.com");

    }
}
