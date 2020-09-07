package com.travia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travia.R
import com.travia.database.entity.WisataEntity
import kotlinx.android.synthetic.main.item_destination.view.*

class WisataAdapter(private val context: Context) :
    RecyclerView.Adapter<WisataAdapter.ViewHolder>() {

    private var destinationList = emptyList<WisataEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(R.layout.item_destination, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(destinationList[position])
    }

    override fun getItemCount(): Int = destinationList.size

    class ViewHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {
        fun bind(item: WisataEntity) {
            containerView.tvNameDestinationItem.text = item.nama
            containerView.tvPriceDestinationItem.text = item.harga
        }
    }

    fun setDestinations(destinationList: List<WisataEntity>) {
        this.destinationList = destinationList
        notifyDataSetChanged()
    }

}