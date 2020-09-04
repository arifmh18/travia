package com.travia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager

class SplasScreen : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splas_screen)
        handler = Handler()
        Handler().postDelayed({
            val intent = Intent(this@SplasScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        },5000)
    }
}