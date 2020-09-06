package com.travia.ui.mitra.add_destination

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.travia.LocationModel
import com.travia.R
import com.travia.WisataModel
import com.travia.databinding.ActivityAddDestinationBinding
import com.travia.ui.mitra.MapActivity
import com.travia.ui.mitra.add_destination.SetScheduleDestinationActivity.Companion.TAG_DESTINATION_DETAIL
import com.travia.utils.LoadingDialogUtil
import com.travia.utils.showToast

class AddDestinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDestinationBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private lateinit var loadingDialogUtil: LoadingDialogUtil

    private val destinationCategory = listOf("Wisata Kota", "Wisata Alam")

    private var isLocationTaken = false
    private var locationModel: LocationModel? = null

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

//            val adapterCategory = ArrayAdapter(this@AddDestinationActivity,
//                R.layout.item_category, destinationCategory)

//            actDestinationCategory.setAdapter(adapterCategory)

            val adapter =  ArrayAdapter.createFromResource(baseContext, R.array.kategori_wisata_string, R.layout.list_item)

            spinnerCategoryDestination.adapter = adapter

            btnAddLocation.setOnClickListener {
                startActivityForResult(Intent(this@AddDestinationActivity, MapActivity::class.java), REQ_LOCATION)
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
                spinnerCategoryDestination.selectedItemPosition == 0 -> {
                    showToast(this@AddDestinationActivity, "Pilih Kategori Tempat Wisata ")
                }
                !isLocationTaken && locationModel == null -> {
                    Toast.makeText(this@AddDestinationActivity, "Mohon untuk memilih location tempat wisata !", Toast.LENGTH_SHORT).show()
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
                        category = spinnerCategoryDestination.selectedItem.toString(),
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
            video_link = video_link,
            location = locationModel!!
        )

        val intent = Intent(this, SetScheduleDestinationActivity::class.java)
        val wisataString = Gson().toJson(wisataModel)

        loadingDialogUtil.dismiss()

        intent.putExtra(TAG_DESTINATION_DETAIL, wisataString)
        startActivityForResult(intent, 0)

        /*
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

         */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_LOCATION){
            if (resultCode == RESULT_OK){
                if (data != null){
                    val latLng = data.getParcelableExtra<LatLng>(RESULT_LATLNG)

                    val geocoder = Geocoder(this)
                    val listLocation = geocoder.getFromLocation(latLng!!.latitude, latLng.longitude, 1)

                    val locationName = listLocation[0].locality

                    binding.tvDestinationLocation.text = listLocation[0].locality

                    locationModel = LocationModel(name = locationName, latitude = latLng.latitude.toString(), longitude = latLng.longitude.toString())
                    isLocationTaken = true
                }
            }else {
                Toast.makeText(this, "Mohon untuk memilih location tempat wisata !", Toast.LENGTH_SHORT).show()
            }
        }else if (requestCode == ADD_DESTINATION){
            if (resultCode == RESULT_OK){
                finish()
            }else if(resultCode == RESULT_CANCELED){
                Log.d(TAG, "onActivityResult: dibatalkan oleh user")
            }
        }
    }

    companion object {
        const val REQ_LOCATION = 10
        const val ADD_DESTINATION = 0
        const val RESULT_LATLNG = "RESULT_LATLNG"
        var TAG = AddDestinationActivity::class.java.simpleName
    }
}