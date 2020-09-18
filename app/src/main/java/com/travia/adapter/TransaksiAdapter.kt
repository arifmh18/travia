package com.travia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travia.R
import com.travia.database.entity.TransaksiEntity
import kotlinx.android.synthetic.main.item_transaksi.view.*

class TransaksiAdapter(
    private val context: Context, private val listener: (
        TransaksiEntity, status: Int
    ) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.ViewHolders>() {

    var listTransaksi = emptyList<TransaksiEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolders(
        LayoutInflater.from(context).inflate(
            R.layout.item_transaksi, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolders, position: Int) {
        holder.bind(listTransaksi[position], listener)
    }

    override fun getItemCount(): Int {
        return listTransaksi.size
    }

    class ViewHolders(private val container: View) : RecyclerView.ViewHolder(container) {
        fun bind(item: TransaksiEntity, listener: (TransaksiEntity, status: Int) -> Unit) {
            container.apply {
                if (item.foto != "Tidak") {
                    Glide.with(context).load(item.foto).into(imgT)
                }
                txtTNama.text = item.nama
                txtTjumlah.text = item.jumlah.toString()
                txtTTotal.text = item.total.toString()
                plus.setOnClickListener {
                    listener(item, 1)
                }
                minus.setOnClickListener {
                    listener(item, 2)
                }
                deleteThis.setOnClickListener {
                    listener(item, 3)
                }
            }
        }
    }

    fun setData(list: List<TransaksiEntity>) {
        this.listTransaksi = list
        notifyDataSetChanged()
    }
}