package com.travia.ui.wisatawan.list_equipment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.EquipmentModel
import com.travia.R
import com.travia.databinding.ActivityEquipmentListBinding

class EquipmentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEquipmentListBinding

    private lateinit var adapter: EquipmentAdapter

    private lateinit var database: FirebaseDatabase

    private var equipmentType : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        database = FirebaseDatabase.getInstance()

        val intent = intent.extras
        if (intent != null){ // get equipment type
            if (intent.getString(EQUIPMENT_TYPE) == RENT_EQUIPMENT){ // self explanatory
                binding.tvOfferRentEquipmentList.text = getString(R.string.want_rent_equipment)
                equipmentType = intent.getString(EQUIPMENT_TYPE)
            }else if(intent.getString(EQUIPMENT_TYPE) == RENT_VEHICLE){ // self explanatory
                binding.tvOfferRentEquipmentList.text = getString(R.string.want_rent_vehicle)
                equipmentType = intent.getString(EQUIPMENT_TYPE)
            }
        }

        adapter = EquipmentAdapter(this)

        binding.apply {
            tbEquipmentList.setNavigationOnClickListener {
                finish()
            }

            rvEquipmentList.adapter = adapter
            rvEquipmentList.layoutManager = GridLayoutManager(this@EquipmentListActivity, 2)
        }

        getEquipments()
    }

    //get equipments by their type(Alat, Kendaraan)
    private fun getEquipments() {
        if (equipmentType == RENT_EQUIPMENT){ // if type == "Alat"
            database.reference.child("peralatan").orderByChild("kategori").equalTo("Alat")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val equipmentList : ArrayList<EquipmentModel> = ArrayList()
                        for (data in snapshot.children){
                            val equipment = data.getValue(EquipmentModel::class.java)
                            equipmentList.add(equipment!!)
                        }

                        adapter.setEquipments(equipmentList = equipmentList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@EquipmentListActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "onCancelled: ${error.message}")
                    }
                })
        }else if(equipmentType == RENT_VEHICLE){ // if type == "Kendaraan"
            database.reference.child("peralatan").orderByChild("kategori").equalTo("Kendaraan")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val equipmentList : ArrayList<EquipmentModel> = ArrayList()
                        for (data in snapshot.children){
                            val equipment = data.getValue(EquipmentModel::class.java)
                            equipmentList.add(equipment!!)
                        }

                        adapter.setEquipments(equipmentList = equipmentList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@EquipmentListActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "onCancelled: ${error.message}")
                    }
                })
        }
    }

    companion object {
        var EQUIPMENT_TYPE = "EQUIPMENT_TYPE"
        var RENT_VEHICLE = "RENT_VEHICLE"
        var RENT_EQUIPMENT = "RENT_EQUIPMENT"
        var TAG = EquipmentListActivity::class.java.simpleName
    }
}