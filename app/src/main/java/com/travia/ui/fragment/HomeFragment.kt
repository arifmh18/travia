package com.travia.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.synnapps.carouselview.ImageListener
import com.travia.R
import com.travia.WisataModel
import com.travia.database.entity.WisataEntity
import com.travia.databinding.FragmentHomeBinding
import com.travia.ui.CariActivity
import com.travia.ui.wisatawan.DestinationListActivity
import com.travia.viewModel.WisataViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var ref: FirebaseDatabase
    private val viewModel by viewModels<WisataViewModel>()
    private var wisatas: ArrayList<WisataEntity> = ArrayList()

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
        binding.apply {
            carouselViewHome.setImageListener(imageListener)
            carouselViewHome.pageCount = sampleImage.size

            containerCityCategory.setOnClickListener(this@HomeFragment)
            containerNatureCategory.setOnClickListener(this@HomeFragment)
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
        }

        syncData()
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

    companion object {
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
        }
    }
}