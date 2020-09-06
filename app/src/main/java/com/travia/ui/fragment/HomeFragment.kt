package com.travia.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.synnapps.carouselview.ImageListener
import com.travia.R
import com.travia.databinding.FragmentHomeBinding
import com.travia.ui.CariActivity
import com.travia.ui.wisatawan.DestinationListActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

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

        init()
    }

    private fun init() {
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
        }
    }

    var imageListener = ImageListener{position: Int, imageView: ImageView? ->
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