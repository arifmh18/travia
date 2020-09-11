package com.travia.ui.pemandu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.travia.adapter.PemanduAdapter
import com.travia.databinding.ActivityListPemanduBinding
import com.travia.model.PemanduModel
import com.travia.utils.showToast

class ListPemanduActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPemanduBinding
    private lateinit var ref: FirebaseDatabase
    private lateinit var adapter: PemanduAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPemanduBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance()
        getData()
        adapter = PemanduAdapter(this) {
            val i = Intent(this, DetailListActivity::class.java)
            i.putExtra("uid", it.uid)
            startActivity(i)
        }
        initSync()
    }

    private fun getData() {
        ref.getReference("pemandu/").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var pemanduList = ArrayList<PemanduModel>()
                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        val data = h.getValue(PemanduModel::class.java)
                        pemanduList.add(data!!)
                    }
                    adapter.setData(pemanduList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(baseContext, "Error Database!!")
            }

        })
    }

    private fun initSync() {
        binding.apply {
            recylerviewPemandu.layoutManager = GridLayoutManager(this@ListPemanduActivity, 2)
            recylerviewPemandu.adapter = adapter
            tbListPemandu.setNavigationOnClickListener {
                finish()
            }
        }
    }
}