package com.travia.ui.mitra.add_destination

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.model.Image
import com.travia.R
import kotlinx.android.synthetic.main.item_images.view.*
import java.io.File

class AdapterImagesDestination(private val context: Context): RecyclerView.Adapter<AdapterImagesDestination.ViewHolder>() {

    private var imageList = ArrayList<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(
                R.layout.item_images,
                parent,
                false
            )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageList[position])
        holder.itemView.ivDeleteImageItem.setOnClickListener {
            imageList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = imageList.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: Image){
            Glide.with(containerView.context)
                .load(File(item.path))
                .into(containerView.ivImageItem)
        }
    }

    fun addImage(image: Image){
        imageList.add(image)
        notifyDataSetChanged()
    }

    fun getImageList(): List<Image>{
        return imageList
    }

    companion object {
        var TAG = AdapterImagesDestination::class.java.simpleName
    }
}