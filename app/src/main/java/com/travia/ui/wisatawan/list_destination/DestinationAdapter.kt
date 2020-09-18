package com.travia.ui.wisatawan.list_destination

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travia.R
import com.travia.WisataModel
import android.view.View
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.item_destination.view.*

class DestinationAdapter(private val context: Context): RecyclerView.Adapter<DestinationAdapter.ViewHolder> () {

    private var destinationList = emptyList<WisataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(R.layout.item_destination, parent, false)
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(destinationList[position])
    }

    override fun getItemCount(): Int = destinationList.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: WisataModel){
            containerView.tvNameDestinationItem.text = item.nama
            containerView.tvPriceDestinationItem.text = item.harga

            containerView.setOnClickListener {
                val intent = Intent(containerView.context, DetailWisata::class.java)
                intent.putExtra(DetailWisata.ID_WISATA, item.uuid)
                containerView.context.startActivity(intent)
            }
        }
    }

    fun setDestinations(destinationList: List<WisataModel>){
        this.destinationList = destinationList
        notifyDataSetChanged()
    }

}