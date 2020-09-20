package com.travia.ui.wisatawan.list_destination.detail_wisata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.travia.R
import com.travia.WisataModel
import com.travia.adapter.DetailWisataPagerAdapter
import com.travia.databinding.ActivityDetailWisataBinding
import kotlinx.android.synthetic.main.activity_register_mitra.*


class DetailWisata: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailWisataBinding
    private lateinit var database: FirebaseDatabase

    private lateinit var gambarAdapter: GambarAdapter

    private lateinit var detailWisataPagerAdapter: DetailWisataPagerAdapter

//    private lateinit var mMap: GoogleMap

    private val tabTitle = arrayOf("Persyaratan", "Deskripsi")

    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityDetailWisataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getDetailWisata(id = id)
    }

    private fun init(){

        val intent = intent.extras
        if (intent != null){
            id = intent.getString(ID_WISATA)
        }

        gambarAdapter = GambarAdapter(context = this)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.apply {
            recylerviewFoto.apply {
                layoutManager = linearLayoutManager
                adapter = gambarAdapter
            }
            tbDetailDestination.setNavigationOnClickListener {
                finish()
            }
            collapsingDetailDestination.setExpandedTitleColor(ContextCompat.getColor(this@DetailWisata, android.R.color.white))
            collapsingDetailDestination.setCollapsedTitleTextColor(ContextCompat.getColor(this@DetailWisata, android.R.color.transparent))
        }


        database = FirebaseDatabase.getInstance()
//        val mapFragment = SupportMapFragment.newInstance()
//        val fragmentTransaction = supportFragmentManager.beginTransaction()
//
//        mapFragment.getMapAsync(this)
//
//        fragmentTransaction.add(R.id.flCulinaryMapDetail, mapFragment)
//        fragmentTransaction.commit()
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

                detailWisataPagerAdapter = DetailWisataPagerAdapter(this@DetailWisata, wisataModel = getWisataModelString(wisatamodel = wisatamodel))
                viewPagerDetailDestination.adapter = detailWisataPagerAdapter

                TabLayoutMediator(tabLayoutDetailDestination, viewPagerDetailDestination, TabLayoutMediator.TabConfigurationStrategy {
                    tab, position -> tab.text = tabTitle[position]
                }).attach()

                gambarAdapter.setGambarList(wisatamodel.gambar!!)

                collapsingDetailDestination.title = wisatamodel.nama
                tbDetailDestination.title = wisatamodel.nama

//                txtName.text = wisatamodel.nama
//                btnBack.setOnClickListener {
//                    finish()
//                }
//                tvCulinaryLocationDetail.text = wisatamodel.location.name
                rp.text = wisatamodel.harga

                /*
                val latLngIndo = LatLng(wisatamodel.location.latitude.toDouble(), wisatamodel.location.longitude.toDouble())

                mMap.addMarker(
                    MarkerOptions()
                        .position(latLngIndo)
                        .title("Lokasi")
                )

                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngIndo, 18f)
                mMap.moveCamera(cameraUpdate)

                 */

            }
        }
    }

    private fun getWisataModelString(wisatamodel: WisataModel): String {
        return  Gson().toJson(wisatamodel)
    }

    companion object {
        var ID_WISATA= "ID_WISATA"
        var TAG = DetailWisata::class.java.simpleName
    }
    override fun onMapReady(map: GoogleMap?) {
        map ?: return

//        mMap = map
    }
}