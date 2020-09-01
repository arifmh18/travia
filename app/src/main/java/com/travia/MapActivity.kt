package com.travia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.travia.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {

        // init map
        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        mapFragment.getMapAsync(this)

        fragmentTransaction.add(R.id.map, mapFragment)
        fragmentTransaction.commit()

    }

    override fun onMapReady(map: GoogleMap?) {
        map ?: return

        val latLngIndo = LatLng(-0.789275, 113.921327)

        map.addMarker(
            MarkerOptions()
                .position(latLngIndo)
                .title("Pilih lokasi tempat wisata")
        )

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngIndo, 18f)
        map.moveCamera(cameraUpdate)
    }
}