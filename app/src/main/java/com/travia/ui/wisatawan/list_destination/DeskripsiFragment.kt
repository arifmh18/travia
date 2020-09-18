package com.travia.ui.wisatawan.list_destination

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.travia.R
import com.travia.WisataModel
import kotlinx.android.synthetic.main.fragment_deskripsi.*
import kotlinx.android.synthetic.main.fragment_req.*

class DeskripsiFragment : Fragment() {
    private var id: String? = null
    lateinit var fragview: View
    lateinit var list : MutableList<WisataModel>
    private lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragview= inflater.inflate(R.layout.fragment_deskripsi, container, false)
      return fragview
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(id = id)
        init()
    }
    private fun init(){
       val intent = getActivity()?.getIntent()?.getExtras()
        if (intent != null){
           id = intent.getString(ID_WISATA)
        }


    }
   // private fun getDetailWisata(id: String?) {
     //   database.reference.child("wisata").orderByChild("uuid").equalTo(id)
       //     .addValueEventListener(object : ValueEventListener {
         //       override fun onDataChange(snapshot: DataSnapshot) {
          //          var wisatamodel: WisataModel? = null
            //        for (data in snapshot.children) {
        //                wisatamodel = data.getValue(WisataModel::class.java)
            //        }
           //         deskripsi.text = wisatamodel!!.deskripsi

         //   }

          //      override fun onCancelled(error: DatabaseError) {
             //       Log.e(DetailWisata.TAG, "onCancelled: ${error.message}", )
          //      }
        //    })
  // }

    private fun getData(id: String?) {
       ref = FirebaseDatabase.getInstance().getReference()

      list = mutableListOf()
        ref.child("wisata").equalTo(id).addValueEventListener(object :
            ValueEventListener {
          override fun onCancelled(p0: DatabaseError) {
               TODO("not implemented")
           }

           override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()){

                    for (h in p0.children){
                       val wisata = h.getValue(WisataModel::class.java)
                       list.add(wisata!!)
                       deskripsi.text = wisata.deskripsi
                    }

               }
          }
       })
   }
    companion object {
        var ID_WISATA= "uuid"
        var TAG = DetailWisata::class.java.simpleName
   }
}