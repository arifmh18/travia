package com.travia.ui.mitra.add_equipment

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.travia.RequirementModel
import android.view.LayoutInflater
import com.travia.R
import kotlinx.android.synthetic.main.item_set_requirement.view.*

class AdapterRequirement(private val context: Context, private val listener: Listener): RecyclerView.Adapter<AdapterRequirement.ViewHolder>() {

    private var listRequirement = ArrayList<RequirementModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(context)
            .inflate(R.layout.item_set_requirement, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRequirement[position])

        holder.itemView.ivRequirementEdit.setOnClickListener {
            listener.onEditRequirement(
                name = listRequirement[position].nama.toString(),
                required = listRequirement[position].required,
                position = position
            )
        }

        holder.itemView.ivRequirementDelete.setOnClickListener {
            listRequirement.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = listRequirement.size

    class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: RequirementModel){
            containerView.edtRequirementName.text = item.nama
            containerView.tvRequiredRequirement.text = if (item.required)  "Wajib" else "Opsional"
        }
    }

    fun addRequirement(name: String, required: Boolean){
        listRequirement.add(
            RequirementModel(
                nama = name,
                required = required
            )
        )
        notifyDataSetChanged()
    }

    fun editRequirement(name: String, required: Boolean, position: Int){
        listRequirement[position] = RequirementModel(
            nama = name, required = required)
        notifyItemChanged(position)
    }

    fun getRequirement(): List<RequirementModel>{
        return listRequirement
    }

    interface Listener{
        fun onEditRequirement(name: String, required: Boolean, position: Int)
    }
}