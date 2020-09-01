package com.travia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.travia.databinding.ActivityAddDestinationBinding

class AddDestinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDestinationBinding

//    private lateinit var database:

    private val destinationCategory = listOf("Wisata Kota", "Wisata Alam")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        binding.apply {

            val adapterCategory = ArrayAdapter(this@AddDestinationActivity, R.layout.item_category, destinationCategory)

            actDestinationCategory.setAdapter(adapterCategory)

            btnAddDestination.setOnClickListener {
                checkForm()
            }
        }
    }

    private fun checkForm() {
        binding.apply {
            when {
                !tieDestinationName.text.toString().isNotBlank() -> {
                    tilDestinationName.error = "Isikan nama tempat wisata !"   
                }
                !tieDestinationDescription.text.toString().isNotBlank() -> {
                    tilDestinationDescription.error = "Isikan deskripsi tempat wisata"
                }
                !actDestinationCategory.text.isNotBlank() -> {
                    tilDestinationCategory.error = "Pilih kategori tempat wisata"
                }
                !tieDestinationPrice.text.toString().isNotBlank() -> {
                    tilDestinationPrice.error = "Isikan harga masuk tempat wisata"
                }
                !tieDestinationVideoLink.text.toString().isNotBlank() -> {
                    tilDestinationVideoLink.error = "Isikan link video tempat wisata"
                }
                else -> {
                    submitDestination(
                        name = tieDestinationName.text.toString(),
                        description = tieDestinationDescription.text.toString(),
                        category = actDestinationCategory.text.toString(),
                        price = tieDestinationPrice.text.toString(),
                        video_link = tieDestinationVideoLink.text.toString()
                    )
                }
            }
        }
    }

    private fun submitDestination(name: String, description: String, category: String, price: String, video_link: String) {
        val category = when (category){
            "Wisata Alam" -> "alam"
            "Wisata Kota" -> "Kota"
            else -> "alam"
        }


    }
}