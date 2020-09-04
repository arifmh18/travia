package com.travia.ui.mitra.add_equipment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.travia.EquipmentModel
import com.travia.R
import com.travia.databinding.ActivityAddEquipmentBinding
import com.travia.utils.LoadingDialogUtil

class AddEquipmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEquipmentBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var loadingDialogUtil: LoadingDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEquipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        loadingDialogUtil = LoadingDialogUtil(this)

        binding.apply {
            btnAdDEquipment.setOnClickListener {
                checkForm()
            }
        }

    }

    private fun checkForm() {
        binding.apply {
            when {
                !tieEquipmentName.text.toString().isNotBlank() -> {
                    tilEquipmentName.error = "Isi nama peralatan"
                }
                !tieEquipmentDescription.text.toString().isNotBlank() -> {
                    tilEquipmentDescription.error = "Isi deskripsi peralatan"
                }
                !tieEquipmentStock.text.toString().isNotBlank() -> {
                    tilEquipmentStock.error = "Isi stok peralatan"
                }
                !tieEquipmentPrice.text.toString().isNotBlank() -> {
                    tilEquipmentPrice.error = "Isi harga sewa peralatan"
                }
                else -> {
                    submitEquipment(
                        name = tieEquipmentName.text.toString(),
                        description = tieEquipmentDescription.text.toString(),
                        stock = tieEquipmentStock.text.toString(),
                        price = tieEquipmentPrice.text.toString()
                    )
                }
            }
        }
    }

    private fun submitEquipment(name: String, description: String, stock: String, price: String) {
        loadingDialogUtil.show()

        val equipmentModel = EquipmentModel(
            uuid = auth.currentUser?.uid,
            nama = name,
            deskripsi = description,
            stok = stock,
            harga = price
        )

        database.reference.child("peralatan").push()
            .setValue(equipmentModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Berhasil tambah peralatan", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                }else {
                    Toast.makeText(this, "Gagal ${task.exception}", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                }
            }
    }
}