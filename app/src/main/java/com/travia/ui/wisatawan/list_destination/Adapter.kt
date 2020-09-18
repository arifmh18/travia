package com.travia.ui.wisatawan.list_destination

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.travia.R
import com.travia.WisataModel

class Adapter(val mCtx: Context, val layoutResId: Int, val list: List<WisataModel> )
    : ArrayAdapter<WisataModel>(mCtx,layoutResId,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId,null)

        val textNama= view.findViewById<TextView>(R.id.requip)
        val textStatus = view.findViewById<TextView>(R.id.deskripsi)

        val wisata = list[position]

        textNama.text = wisata.nama
        textStatus.text = wisata.deskripsi

        return view

    }
}