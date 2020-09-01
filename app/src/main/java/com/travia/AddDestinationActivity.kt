package com.travia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.travia.databinding.ActivityAddDestinationBinding
import com.travia.utils.LoadingDialogUtil

class AddDestinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDestinationBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private lateinit var loadingDialogUtil: LoadingDialogUtil

    private val destinationCategory = listOf("Wisata Kota", "Wisata Alam")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        loadingDialogUtil = LoadingDialogUtil(this)

        binding.apply {

            val adapterCategory = ArrayAdapter(this@AddDestinationActivity, R.layout.item_category, destinationCategory)

            actDestinationCategory.setAdapter(adapterCategory)

            btnAddLocation.setOnClickListener {
                startActivity(Intent(this@AddDestinationActivity, MapActivity::class.java))
            }

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
                    loadingDialogUtil.show()
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

        if (auth.currentUser == null) return

        val mCategory = when (category){
            "Wisata Alam" -> "alam"
            "Wisata Kota" -> "Kota"
            else -> "alam"
        }

        val wisataModel = WisataModel(
            uuid = auth.currentUser?.uid.toString(),
            nama = name,
            deskripsi = description,
            kategory = mCategory,
            harga = price,
            video_link = video_link
        )

        database.reference.child("wisata").push()
            .setValue(wisataModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Berhasil tambah wisata", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                }else {
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                }
            }
    }
}