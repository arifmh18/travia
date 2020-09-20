package com.travia.ui.wisatawan.list_destination.detail_wisata

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import com.travia.R
import com.travia.RequirementModel
import kotlinx.android.synthetic.main.item_requirement.view.*

class RequirementAdapter(private val context: Context): RecyclerView.Adapter<RequirementAdapter.ViewHolder>() {

    private var requirementList = emptyList<RequirementModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(
                R.layout.item_requirement,
                parent,
                false
            )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(requirementList[position])
    }

    override fun getItemCount() = requirementList.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: RequirementModel){
            containerView.tv_name_item_requirement.text = item.nama
            when(item.required){
                true -> containerView.tv_required_item_requirement.text = "Wajib"
                else -> containerView.tv_required_item_requirement.text = "Opsional"
            }
        }
    }

    fun setRequirements(requirementList: List<RequirementModel>){
        this.requirementList = requirementList
        notifyDataSetChanged()
    }
}