package com.travia.ui.wisatawan.list_equipment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travia.EquipmentModel
import com.travia.R
import kotlinx.android.synthetic.main.item_destination.view.*

class EquipmentAdapter (private val context: Context): RecyclerView.Adapter<EquipmentAdapter.ViewHolder>(){

    private var equipmentList = emptyList<EquipmentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(
                R.layout.item_destination,
                parent,
                false
            )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(equipmentList[position])
    }

    override fun getItemCount() = equipmentList.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: EquipmentModel){
            containerView.tvNameDestinationItem.text = item.nama
            containerView.tvPriceDestinationItem.text = item.harga
        }
    }

    fun setEquipments(equipmentList: List<EquipmentModel>){
        this.equipmentList = equipmentList
        notifyDataSetChanged()
    }
}