package com.travia.ui.wisatawan.list_destination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.R
import com.travia.WisataModel
import com.travia.databinding.ActivityDetailWisataBinding


class DetailWisata: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailWisataBinding
    private lateinit var database: FirebaseDatabase

    private lateinit var mMap: GoogleMap

    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityDetailWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
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

    }
}