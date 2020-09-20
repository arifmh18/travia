package com.travia.ui.wisatawan.list_destination.detail_wisata

import android.view.View
import android.view.LayoutInflater
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travia.R
import com.travia.utils.hide
import kotlinx.android.synthetic.main.item_images.view.*

class GambarAdapter(private val context: Context): RecyclerView.Adapter<GambarAdapter.ViewHolder>() {

    private var gambarList = emptyList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(
                R.layout.item_images,
                parent,
            false
            )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(gambarList[position])
    }

    override fun getItemCount() = gambarList.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: String){

            containerView.ivDeleteImageItem.hide()

            Glide.with(containerView.context)
                .load(item)
                .into(containerView.ivImageItem)
        }
    }

    fun setGambarList(gambarList: List<String>){
        this.gambarList = gambarList
        notifyDataSetChanged()
    }
}