package com.travia.ui.wisatawan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.travia.R
import com.travia.databinding.ActivityEquipmentListBinding

class EquipmentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEquipmentListBinding

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        database = FirebaseDatabase.getInstance()

        val intent = intent.extras
        if (intent != null){
            if (intent.getString(EQUIPMENT_TYPE) == RENT_EQUIPMENT){
                binding.tvOfferRentEquipmentList.text = getString(R.string.want_rent_equipment)
            }else if(intent.getString(EQUIPMENT_TYPE) == RENT_VEHICLE){
                binding.tvOfferRentEquipmentList.text = getString(R.string.want_rent_vehicle)
            }
        }

        binding.apply {
            tbEquipmentList.setNavigationOnClickListener {
                finish()
            }
        }
    }

    companion object {
        var EQUIPMENT_TYPE = "EQUIPMENT_TYPE"
        var RENT_VEHICLE = "RENT_VEHICLE"
        var RENT_EQUIPMENT = "RENT_EQUIPMENT"
    }
}