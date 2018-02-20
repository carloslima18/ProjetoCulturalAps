package com.example.carlos.projetocultural

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.carlos.projetocultural.extensions.toast
import java.util.*
import kotlin.concurrent.timerTask


class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val timer = Timer()
        timer.schedule(timerTask {
            toast("bem vindo")
        },3000)
        finish()
    }
}
