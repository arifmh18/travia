package com.travia.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.travia.R

class LoadingDialogUtil(private var context: Context) {

    lateinit var alertDialogBuilder : AlertDialog.Builder
    lateinit var alertDialog: AlertDialog

    fun show(){
        val view = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null, false)

        alertDialogBuilder = AlertDialog.Builder(context)
            .setCancelable(false)
            .setView(view)

        alertDialog = alertDialogBuilder.create()

        alertDialog.show()
    }

    fun dismiss(){
        if (alertDialog.isShowing){
            alertDialog.dismiss()
        }
    }

}