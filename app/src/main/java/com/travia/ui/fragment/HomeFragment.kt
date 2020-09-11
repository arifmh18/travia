package com.travia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.synnapps.carouselview.ImageListener
import com.travia.R
import com.travia.WisataModel
import com.travia.database.entity.WisataEntity
import com.travia.databinding.FragmentHomeBinding
import com.travia.ui.CariActivity
import com.travia.ui.wisatawan.list_destination.DestinationListActivity
import com.travia.ui.wisatawan.list_equipment.EquipmentListActivity
import com.travia.ui.wisatawan.list_destination.DestinationAdapter
import com.travia.ui.wisatawan.list_kuliner.CulinaryListActivity
import com.travia.viewModel.WisataViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var ref: FirebaseDatabase
    private val viewModel by viewModels<WisataViewModel>()
    private var wisatas: ArrayList<WisataEntity> = ArrayList()

    private lateinit var adapter: DestinationAdapter

    private lateinit var database: FirebaseDatabase

    private val sampleImage = arrayOf(
        R.drawable.wisata_pujon_kidul,
        R.drawable.bromo_tengger_semeru_national_park,
        R.drawable.lawang_sewu
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance()
        init()
    }

    private fun init() {
        viewModel.init(requireContext())

        database = FirebaseDatabase.getInstance()

        adapter = DestinationAdapter(context = requireContext())

        binding.apply {
            carouselViewHome.setImageListener(imageListener)
            carouselViewHome.pageCount = sampleImage.size

            containerCityCategory.setOnClickListener(this@HomeFragment)
            containerNatureCategory.setOnClickListener(this@HomeFragment)
            containerCulinary.setOnClickListener(this@HomeFragment)
            edtCari.isFocusable = false
            cari.setOnClickListener {
                startActivity(Intent(context, CariActivity::class.java))
            }
            logoCari.setOnClickListener {
                startActivity(Intent(context, CariActivity::class.java))
            }
            edtCari.setOnClickListener {
                startActivity(Intent(context, CariActivity::class.java))
            }
            edtCari.addTextChangedListener {
                startActivity(Intent(context, CariActivity::class.java))
            }
            homeCardSearch.setOnClickListener {
                startActivity(Intent(context, CariActivity::class.java))
            }
            containerRentVehicle.setOnClickListener(this@HomeFragment)
            containerRentEquipment.setOnClickListener(this@HomeFragment)

            rvPopularDestination.layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            rvPopularDestination.adapter = adapter

        }

        syncData()

        loadRecomendedDestination() // get destination by "rekomendasi = true"

    }

    private fun syncData() {
        ref.getReference("wisata/").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (value in snapshot.children) {
                        val item = value.getValue(WisataModel::class.java)
                        val data = WisataEntity(
                            item!!.uuid,
                            item.nama,
                            item.deskripsi,
                            item.kategory,
                            item.harga,
                            item.video_link,
                            item.location.name,
                            item.rekomendasi
                        )
                        wisatas.add(data)
                    }
                    viewModel.insertAll(wisatas)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    var imageListener = ImageListener { position: Int, imageView: ImageView? ->
        imageView?.setImageResource(sampleImage[position])
    }

    //get destination by "rekomendasi = true"
    private fun loadRecomendedDestination() {
        database.reference.child("wisata").orderByChild("rekomendasi").equalTo(true)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val destinationList: ArrayList<WisataModel> = ArrayList()
                    for (data in snapshot.children) {
                        val wisata = data.getValue(WisataModel::class.java)
                        destinationList.add(wisata!!)
                    }

                    adapter.setDestinations(destinationList = destinationList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.containerCityCategory -> {
                val intent = Intent(requireContext(), DestinationListActivity::class.java)
                intent.putExtra(DestinationListActivity.DESTINATION_TYPE, DestinationListActivity.CITY_TYPE)
                intent.putExtra(DestinationListActivity.TOOLBAR_TITLE, "Wisata Kota")
                startActivity(intent)
            }
            R.id.containerNatureCategory -> {
                val intent = Intent(requireContext(), DestinationListActivity::class.java)
                intent.putExtra(DestinationListActivity.DESTINATION_TYPE, DestinationListActivity.NATURE_TYPE)
                intent.putExtra(DestinationListActivity.TOOLBAR_TITLE, "Wisata Alam")
                startActivity(intent)
            }
            R.id.containerRentVehicle -> {
                val intent = Intent(requireContext(), EquipmentListActivity::class.java)
                intent.putExtra(EquipmentListActivity.EQUIPMENT_TYPE, EquipmentListActivity.RENT_VEHICLE)
                startActivity(intent)
            }
            R.id.containerRentEquipment -> {
                val intent = Intent(requireContext(), EquipmentListActivity::class.java)
                intent.putExtra(EquipmentListActivity.EQUIPMENT_TYPE, EquipmentListActivity.RENT_EQUIPMENT)
                startActivity(intent)
            }
            R.id.containerCulinary -> {
                startActivity(Intent(requireContext(), CulinaryListActivity::class.java))
            }
        }
    }

    companion object {
        var TAG = HomeFragment::class.java.simpleName
    }
}