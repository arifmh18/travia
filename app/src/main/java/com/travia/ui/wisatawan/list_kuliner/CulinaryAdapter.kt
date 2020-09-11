package com.travia.ui.wisatawan.list_kuliner

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travia.R
import com.travia.model.Culinary
import kotlinx.android.synthetic.main.item_culinary.view.*

class CulinaryAdapter(private val context: Context): RecyclerView.Adapter<CulinaryAdapter.ViewHolder>() {

    private var culinaryList = emptyList<Culinary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(
                R.layout.item_culinary,
                parent,
                false
            )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(culinaryList[position])
    }

    override fun getItemCount(): Int = culinaryList.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: Culinary){
            containerView.tvNameCulinaryItem.text = item.nama
            containerView.tvDeskripsiCulinaryItem.text = item.deskripsi
            containerView.tvLocationNameCulinaryItem.text = item.lokasi.name

            containerView.setOnClickListener {
                val intent = Intent(containerView.context, DetailCulinaryActivity::class.java)
                intent.putExtra(DetailCulinaryActivity.ID_CULINARY, item.id_wisata)
                containerView.context.startActivity(intent)
            }

            Glide.with(containerView.context)
                .load(item.gambar)
                .centerCrop()
                .into(containerView.ivCulinaryItem)
        }
    }

    fun setCulinaries(mCulinaryList: List<Culinary>){
        this.culinaryList = mCulinaryList
        notifyDataSetChanged()
    }
}