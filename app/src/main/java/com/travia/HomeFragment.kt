package com.travia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.synnapps.carouselview.ImageListener
import com.travia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

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
            carouselViewHome.pageCount = sampleImage.size
            carouselViewHome.setImageListener(imageListener)
        }
    }

    var imageListener = ImageListener{position: Int, imageView: ImageView? ->
        imageView?.setImageResource(sampleImage[position])
    }

    companion object {
    }
}