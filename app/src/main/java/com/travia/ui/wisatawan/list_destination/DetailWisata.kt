package com.travia.ui.wisatawan.list_destination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.R
import com.travia.WisataModel
import com.travia.databinding.ActivityDetailWisataBinding
import kotlinx.android.synthetic.main.activity_detail_wisata.*
import kotlinx.android.synthetic.main.fragment_deskripsi.*


class DetailWisata: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailWisataBinding
    private lateinit var database: FirebaseDatabase
    var fragment: Fragment? = null
    private lateinit var mMap: GoogleMap

    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityDetailWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        tabdetail!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> fragment=ReqFragment ()
                    1 -> fragment= DeskripsiFragment ()

                }
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.recylerview_detail, fragment!!)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.commit()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
    private fun init(){
        val intent = intent.extras
        if (intent != null){
            id = intent.getString(ID_WISATA)
        }

        database = FirebaseDatabase.getInstance()
        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        mapFragment.getMapAsync(this)

        fragmentTransaction.add(R.id.flCulinaryMapDetail, mapFragment)
        fragmentTransaction.commit()
    }
    private fun getDetailWisata(id: String?) {
        database.reference.child("wisata").orderByChild("uuid").equalTo(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var wisatamodel: WisataModel? = null
                    for (data in snapshot.children) {
                        wisatamodel = data.getValue(WisataModel::class.java)
                    }

                    setData(wisatamodel= wisatamodel)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}", )
                }
            })
    }
    private fun setData(wisatamodel: WisataModel?) {
        binding.apply {

            if (wisatamodel != null){
                Glide.with(this@DetailWisata)

                tvCulinaryLocationDetail.text = wisatamodel.location.name
                rp.text = wisatamodel.harga


                val latLngIndo = LatLng(wisatamodel.location.latitude.toDouble(), wisatamodel.location.longitude.toDouble())

                mMap.addMarker(
                    MarkerOptions()
                        .position(latLngIndo)
                        .title("Lokasi")
                )

                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngIndo, 18f)
                mMap.moveCamera(cameraUpdate)

            }
        }
    }

    companion object {
        var ID_WISATA= "ID_WISATA"
        var TAG = DetailWisata::class.java.simpleName
    }
    override fun onMapReady(map: GoogleMap?) {
        map ?: return

        mMap = map
        getDetailWisata(id = id)
//halo
    }
}