package com.travia

import android.content.Intent
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

    private lateinit var mMap: GoogleMap

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

        binding.apply {
            tbMap.setNavigationOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
            btnSetLocationMap.setOnClickListener {
                val latLng = mMap.cameraPosition.target

                val data = Intent()
                data.putExtra(AddDestinationActivity.RESULT_LATLNG, latLng)
                setResult(RESULT_OK, data)
                finish()

            }
        }

    }

    override fun onMapReady(map: GoogleMap?) {
        map ?: return

        mMap = map
        val latLngIndo = LatLng(-0.789275, 113.921327)

        map.addMarker(
            MarkerOptions()
                .position(latLngIndo)
                .alpha(0f)
                .title("Pilih location tempat wisata")
        )

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngIndo, 18f)
        map.moveCamera(cameraUpdate)

        map.setOnCameraMoveListener {
            binding.btnSetLocationMap.isEnabled = false
            binding.btnSetLocationMap.isClickable = false
        }

        map.setOnCameraIdleListener {
            binding.btnSetLocationMap.isEnabled = true
            binding.btnSetLocationMap.isClickable = true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }
}