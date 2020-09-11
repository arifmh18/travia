package com.travia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.travia.R
import com.travia.model.PemanduModel
import kotlinx.android.synthetic.main.item_pemandu.view.*

class PemanduAdapter(private var context: Context, private var listener: (PemanduModel) -> Unit) :
    RecyclerView.Adapter<PemanduAdapter.ViewHolders>() {

    var listPemandu = emptyList<PemanduModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolders(
        LayoutInflater.from(context)
            .inflate(R.layout.item_pemandu, parent, false)
    )

    override fun onBindViewHolder(holder: PemanduAdapter.ViewHolders, position: Int) {
        holder.bind(listPemandu[position], listener)
    }

    override fun getItemCount(): Int {
        return listPemandu.size
    }

    class ViewHolders(private val contaiter: View) : RecyclerView.ViewHolder(contaiter) {
        var auth = FirebaseAuth.getInstance()

        fun bind(item: PemanduModel, listener: (PemanduModel) -> Unit) {
            if (item.foto != "Tidak") {
                Glide.with(contaiter).load(item.foto).into(contaiter.imgPemandu)
            } else if (item.foto == "Google") {
                Glide.with(contaiter).load(auth.currentUser!!.photoUrl).into(contaiter.imgPemandu)
            }
            contaiter.namaPemandu.setText(item.nama)
            contaiter.hargaPerwaktuP.setText(item.harga)
            contaiter.deskPemandu.setText(item.moto)
            contaiter.goToDetail.setOnClickListener {
                listener(item)
            }
        }
    }

    fun setData(pemanduList: List<PemanduModel>) {
        this.listPemandu = pemanduList
        notifyDataSetChanged()
    }

}