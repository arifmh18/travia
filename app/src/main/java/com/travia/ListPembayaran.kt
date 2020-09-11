package com.travia

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_list_pembayaran.*

class ListPembayaran : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pembayaran)
        //back end menyambungkan apps gojek dana ovo berhasil 
        gopay.setOnClickListener {
            val isAppInstalled: Boolean = appInstalledOrNot("com.gojek.app")
            if (isAppInstalled) {
                val launchGojek = packageManager.getLaunchIntentForPackage("com.gojek.app")
                startActivity(launchGojek)
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=com.gojek.app")
                startActivity(intent)
            }
        }
        dana.setOnClickListener {
            val isAppInstalled: Boolean = appInstalledOrNot("id.dana")
            if (isAppInstalled) {
                val launchDana = packageManager.getLaunchIntentForPackage("id.dana")
                startActivity(launchDana)
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=id.dana")
                startActivity(intent)
            }
        }
        ovo.setOnClickListener {
            val isAppInstalled: Boolean = appInstalledOrNot("ovo.id")
            if (isAppInstalled) {
                val launchOvo = packageManager.getLaunchIntentForPackage("ovo.id")
                startActivity(launchOvo)
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=ovo.id")
                startActivity(intent)
            }
        }

        icon_naviga.setOnClickListener {
            onBackPressed()
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false

    }
}