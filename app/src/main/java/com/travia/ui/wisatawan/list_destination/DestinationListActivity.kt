package com.travia.ui.wisatawan.list_destination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.WisataModel
import com.travia.databinding.ActivityDestinationListBinding
import com.travia.ui.wisatawan.list_destination.DestinationAdapter
import com.travia.utils.hide

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
            binding.tbDestinationList.title = intent.getString(TOOLBAR_TITLE)
        }

        adapter = DestinationAdapter(this){

        }

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
                        val destinationList: ArrayList<WisataModel> = ArrayList()
                        for (data in snapshot.children) {
                            val wisata = data.getValue(WisataModel::class.java)
                            destinationList.add(wisata!!)
                        }

                        adapter.setDestinations(destinationList = destinationList)
                        binding.pbLoadDestinationList.hide()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DestinationListActivity, error.message, Toast.LENGTH_SHORT).show()
                        binding.pbLoadDestinationList.hide()
                    }
                })
        }else if(destinationType == NATURE_TYPE){
            database.reference.child("wisata").orderByChild("kategory").equalTo("alam")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val destinationList: ArrayList<WisataModel> = ArrayList()
                        for (data in snapshot.children) {
                            val wisata = data.getValue(WisataModel::class.java)
                            destinationList.add(wisata!!)
                        }

                        adapter.setDestinations(destinationList = destinationList)
                        binding.pbLoadDestinationList.hide()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DestinationListActivity, error.message, Toast.LENGTH_SHORT).show()
                        binding.pbLoadDestinationList.hide()
                    }
                })
        }
    }

    companion object {
        val DESTINATION_TYPE = "DESTINATION_TYPE"
        val TOOLBAR_TITLE = "TOOLBAR_TITLE"
        var CITY_TYPE = 1
        var NATURE_TYPE = 2
        var TAG = DestinationListActivity::class.java.simpleName
    }

}