package com.travia.ui.wisatawan.list_kuliner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.databinding.ActivityCulinaryListBinding
import com.travia.model.Culinary
import com.travia.utils.hide

class CulinaryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCulinaryListBinding

    private lateinit var adapter: CulinaryAdapter

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCulinaryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        database = FirebaseDatabase.getInstance()

        binding.apply {

            adapter = CulinaryAdapter(this@CulinaryListActivity)

            tbCulinaryList.setNavigationOnClickListener {
                finish()
            }

            rvCulinaryList.layoutManager = LinearLayoutManager(this@CulinaryListActivity)
            rvCulinaryList.adapter = adapter
        }

        getCulinary()
    }

    private fun getCulinary() {
        database.reference.child("kuliner").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val culinaryList: ArrayList<Culinary> = ArrayList()
                for (data in snapshot.children){
                    val culinary = data.getValue(Culinary::class.java)
                    culinaryList.add(culinary!!)
                }

                adapter.setCulinaries(mCulinaryList = culinaryList)
                binding.pbLoadCulinaryList.hide()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}