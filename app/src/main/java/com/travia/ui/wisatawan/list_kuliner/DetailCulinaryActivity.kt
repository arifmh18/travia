package com.travia.ui.wisatawan.list_kuliner

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
import com.travia.databinding.ActivityDetailCulinaryBinding
import com.travia.model.Culinary

class DetailCulinaryActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailCulinaryBinding

    private lateinit var database: FirebaseDatabase

    private lateinit var mMap: GoogleMap

    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCulinaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        val intent = intent.extras
        if (intent != null){
            id = intent.getString(ID_CULINARY)
        }

        database = FirebaseDatabase.getInstance()

        binding.apply {
            tbDetailCulinary.setNavigationOnClickListener {
                finish()
            }
        }

        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        mapFragment.getMapAsync(this)

        fragmentTransaction.add(R.id.flCulinaryMapDetail, mapFragment)
        fragmentTransaction.commit()
    }

    private fun getDetailCuliary(id: String?) {
        database.reference.child("kuliner").orderByChild("id_wisata").equalTo(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var culinary: Culinary? = null
                    for (data in snapshot.children) {
                        culinary = data.getValue(Culinary::class.java)
                    }

                    setData(culinary = culinary)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}", )
                }
            })
    }

    private fun setData(culinary: Culinary?) {
        binding.apply {

            if (culinary != null){
                Glide.with(this@DetailCulinaryActivity)
                    .load(culinary.gambar)
                    .into(ivDetailCulinary)

                tvCulinaryNameDetail.text = culinary.nama
                tvCulinaryDescriptionDetail.text = culinary.deskripsi
                tvCulinaryLocationDetail.text = culinary.lokasi.name

                val latLngIndo = LatLng(culinary.lokasi.latitude.toDouble(), culinary.lokasi.longitude.toDouble())

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
        var ID_CULINARY = "ID_CULINARY"
        var TAG = DetailCulinaryActivity::class.java.simpleName
    }

    override fun onMapReady(map: GoogleMap?) {
        map ?: return

        mMap = map
        getDetailCuliary(id = id)

    }
}