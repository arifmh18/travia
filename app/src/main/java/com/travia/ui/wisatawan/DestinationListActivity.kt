package com.travia.ui.wisatawan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.R
import com.travia.databinding.ActivityDestinationListBinding
import com.travia.ui.wisatawan.list_destination.DestinationAdapter

class DestinationListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDestinationListBinding

    private lateinit var database: FirebaseDatabase

    private lateinit var adapter: DestinationAdapter

    private var destinationType = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        database = FirebaseDatabase.getInstance()

        val intent = intent.extras
        if (intent != null){
            destinationType = intent.getInt(DESTINATION_TYPE)
        }

        adapter = DestinationAdapter(this)

        binding.apply {
            tbDestinationList.setNavigationOnClickListener {
                finish()
            }

            rvDestinationList.layoutManager = GridLayoutManager(this@DestinationListActivity, 2)
            rvDestinationList.adapter = adapter
        }

        getDestinationList()
    }

    private fun getDestinationList() {

        if (destinationType == CITY_TYPE){
            database.reference.child("wisata").orderByChild("kategory").equalTo("Kota")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            Log.d(TAG, "onDataChange: $data")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d(TAG, "onCancelled: ${error.toException()}")
                    }
                })
        }
    }

    companion object {
        val DESTINATION_TYPE = "DESTINATION_TYPE"
        var CITY_TYPE = 1
        var NATURE_TYPE = 2
        var TAG = DestinationListActivity::class.java.simpleName
    }

}