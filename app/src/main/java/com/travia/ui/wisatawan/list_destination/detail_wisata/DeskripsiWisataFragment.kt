    package com.travia.ui.wisatawan.list_destination.detail_wisata

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.travia.R
import com.travia.WisataModel
import com.travia.databinding.FragmentDeskripsiWisataBinding

class DeskripsiWisataFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDeskripsiWisataBinding

    private lateinit var mMap: GoogleMap

    private lateinit var wisataModel: WisataModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeskripsiWisataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        arguments?.apply {
            getString(TAG_WISATA_MODEL)?.let {
                Log.d(TAG, "init: $it")
                wisataModel = getWisataModel(wisataModelString = it)
            }
        }

        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction = childFragmentManager.beginTransaction()

        mapFragment.getMapAsync(this)

        fragmentTransaction.add(R.id.frame_maps_detail_destination, mapFragment)
        fragmentTransaction.commit()
    }

    private fun setData() {
        Log.d(TAG, "setData()")
        binding.apply {
            tvDescriptionDetailDestination.text = wisataModel.deskripsi
            tvLocationDetailDestination.text = wisataModel.location.name

            val latLng = LatLng(wisataModel.location.latitude.toDouble(), wisataModel.location.longitude.toDouble())

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Lokasi")
            )

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18f)
            mMap.moveCamera(cameraUpdate)

        }
    }

    private fun getWisataModel(wisataModelString: String): WisataModel {
        return Gson().fromJson(wisataModelString, WisataModel::class.java)
    }

    override fun onMapReady(map: GoogleMap?) {
        map ?: return

        mMap = map
        setData()
    }

    companion object {
        var TAG = DeskripsiWisataFragment::class.java.simpleName

        var TAG_WISATA_MODEL = "wisataModel"

        fun newInstance(wisataModel: String): DeskripsiWisataFragment {
            val bundle = Bundle().apply {
                putString(TAG_WISATA_MODEL, wisataModel)
            }

            return DeskripsiWisataFragment().apply {
                arguments = bundle
            }
        }
    }
}