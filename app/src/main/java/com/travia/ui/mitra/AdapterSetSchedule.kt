package com.travia.ui.mitra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travia.DayScheduleModel
import com.travia.R
import com.travia.ScheduleModel
import kotlinx.android.synthetic.main.item_set_schedule.view.*

class AdapterSetSchedule(private val context: Context, private val listener: Listener) : RecyclerView.Adapter<AdapterSetSchedule.ViewHolder> (){

    private var listSchedules = emptyList<DayScheduleModel>()
    private val listOfDay = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(
            context
        ).inflate(R.layout.item_set_schedule, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSchedules[position])
        holder.itemView.edtOpenTimeItemSetShedule.setOnClickListener {
            listener.onChooseOpenTime(position = position)
        }
        holder.itemView.edtCloseTimeItemSetShedule.setOnClickListener {
            listener.onChooseCloseTime(position = position)
        }
    }

    override fun getItemCount(): Int = listSchedules.size

    inner class ViewHolder(private val containerView: View): RecyclerView.ViewHolder(containerView) {
        fun bind(item: DayScheduleModel){
            containerView.tvDayNameItemSetShedule.text = listOfDay[layoutPosition]
            containerView.edtOpenTimeItemSetShedule.setText(item.buka)
            containerView.edtCloseTimeItemSetShedule.setText(item.tutup)
        }
    }

    fun setListSchedule(listSchedule: ArrayList<DayScheduleModel>){
        listSchedules = listSchedule
        notifyDataSetChanged()
    }

    fun setOpenTime(open: String, position: Int){
        listSchedules[position].buka = open
        notifyItemChanged(position)
    }

    fun setCloseTime(close: String, position: Int){
        listSchedules[position].tutup = close
        notifyItemChanged(position)
    }

    fun getScheduleList(): List<DayScheduleModel>{
        return listSchedules
    }

    interface Listener {
        fun onChooseOpenTime(position: Int)
        fun onChooseCloseTime(position: Int)
    }

}