package com.travia.ui.wisatawan.list_destination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.travia.R
import com.travia.WisataModel
import kotlinx.android.synthetic.main.fragment_req.*

class ReqFragment : Fragment() {
    lateinit var fragview: View
    private var id: String? = null
    lateinit var list : MutableList<WisataModel>

    lateinit var ref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragview = inflater.inflate(R.layout.fragment_req, container, false)
        return fragview
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()

    }

    private fun getData() {
        ref = FirebaseDatabase.getInstance().getReference()

        list = mutableListOf()
        ref.child("wisata").orderByChild("uuid").equalTo(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    for (h in p0.children){
                        val wisata = h.getValue(WisataModel::class.java)
                        list.add(wisata!!)
                    }
                    val adapter = Adapter(context!!,R.layout.item_req,list)
                    listView.adapter = adapter
                }
            }
        })
    }
}