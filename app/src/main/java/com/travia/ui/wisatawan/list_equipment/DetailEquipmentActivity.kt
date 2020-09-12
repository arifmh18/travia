package com.travia.ui.wisatawan.list_equipment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.EquipmentModel
import com.travia.R
import com.travia.databinding.ActivityDetailEquipmentBinding

class DetailEquipmentActivity : AppCompatActivity() {

    private var equipmentId: String? = null

    private lateinit var binding: ActivityDetailEquipmentBinding

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEquipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getDetailEquipment()
    }

    private fun init() {

        val intent = intent.extras
        if (intent != null){
            equipmentId = intent.getString(EQUIPMENT_ID)
        }

        database = FirebaseDatabase.getInstance()

        binding.apply {
            tbDetailEquipment.setNavigationOnClickListener {
                finish()
            }

            collapsingDetailEquipment.setExpandedTitleColor(ContextCompat.getColor(this@DetailEquipmentActivity, android.R.color.transparent))
            collapsingDetailEquipment.setCollapsedTitleTextColor(ContextCompat.getColor(this@DetailEquipmentActivity, R.color.white))
        }
    }

    private fun getDetailEquipment() {
        database.reference.child("peralatan").orderByChild("uuid").equalTo(equipmentId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var equipment: EquipmentModel? = null
                    for (data in snapshot.children) {
                        equipment = data.getValue(EquipmentModel::class.java)
                    }

                    setData(equipment = equipment)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled: ${error.message}")
                }
            })
    }

    private fun setData(equipment: EquipmentModel?) {
        binding.apply {
            if (equipment != null){
                tvEquipmentNameDetail.text = equipment.nama
                tvEquipmentDescriptionDetail.text = equipment.deskripsi
                tvEquipmentPriceDetail.text = equipment.harga
                tvEquipmentStokDetail.text = equipment.stok
            }
        }
    }

    companion object {
        var EQUIPMENT_ID = "EQUIPMENT_ID"
        var TAG = DetailEquipmentActivity::class.java.simpleName
    }
}