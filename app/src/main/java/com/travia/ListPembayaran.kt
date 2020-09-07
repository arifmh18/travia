package com.travia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_pembayaran.*

class ListPembayaran : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pembayaran)
        gopay.setOnClickListener {
            val launchGojek = packageManager.getLaunchIntentForPackage("com.gojek.app")
            startActivity(launchGojek)
            finish()
        }
        dana.setOnClickListener {
            val launchDana = packageManager.getLaunchIntentForPackage("id.dana")
            startActivity(launchDana)
            finish()
        }
        ovo.setOnClickListener {
            val launchOvo = packageManager.getLaunchIntentForPackage("ovo.id")
            startActivity(launchOvo)
            finish()
        }
    }
}